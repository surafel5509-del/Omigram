package com.example.data.remote.supabase

import com.example.core.model.Chat
import com.example.core.model.Message
import com.example.core.model.MessageStatus
import com.example.core.model.User
import com.example.domain.repository.ChatRepository
import com.example.domain.repository.MessageRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.PrimaryKey
import io.github.jan.supabase.realtime.postgresListDataFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
private data class ChatRow(
    val id: String,
    val type: String = "direct",
    val title: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)

@Serializable
private data class ChatInsert(
    val type: String = "direct",
    val title: String? = null
)

@Serializable
private data class ChatMemberRow(
    @SerialName("chat_id") val chatId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("is_muted") val isMuted: Boolean = false,
    @SerialName("is_pinned") val isPinned: Boolean = false
)

@Serializable
private data class MembershipInsert(
    @SerialName("chat_id") val chatId: String,
    @SerialName("user_id") val userId: String
)

@Serializable
private data class MessageRow(
    val id: String,
    @SerialName("chat_id") val chatId: String,
    @SerialName("sender_id") val senderId: String,
    val content: String,
    @SerialName("message_type") val messageType: String = "TEXT",
    @SerialName("attachment_url") val attachmentUrl: String? = null,
    @SerialName("reply_to_id") val replyToId: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("edited_at") val editedAt: String? = null,
    @SerialName("deleted_at") val deletedAt: String? = null
)

@Serializable
private data class MessageInsert(
    @SerialName("chat_id") val chatId: String,
    @SerialName("sender_id") val senderId: String,
    val content: String,
    @SerialName("message_type") val messageType: String = "TEXT",
    @SerialName("attachment_url") val attachmentUrl: String? = null,
    @SerialName("reply_to_id") val replyToId: String? = null
)

@Serializable
private data class ProfileRow(
    val id: String,
    val username: String,
    @SerialName("full_name") val fullName: String,
    val bio: String = "",
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("is_online") val isOnline: Boolean = false
)

private fun ProfileRow.toUser() = User(
    id = id,
    username = username,
    fullName = fullName,
    bio = bio,
    avatarUrl = avatarUrl.orEmpty(),
    isOnline = isOnline
)

private fun MessageRow.toMessage(sender: User?) = Message(
    id = id,
    chatId = chatId,
    senderId = senderId,
    senderName = sender?.fullName ?: "",
    content = content,
    attachmentUrl = attachmentUrl.orEmpty(),
    replyToMessageId = replyToId,
    status = if (deletedAt == null) MessageStatus.SENT else MessageStatus.READ,
    createdAt = Instant.parse(createdAt).toEpochMilliseconds(),
    updatedAt = Instant.parse(editedAt ?: createdAt).toEpochMilliseconds(),
    deletedAt = deletedAt?.let(Instant::parse)?.toEpochMilliseconds()
)

class SupabaseLiveMessageRepository : MessageRepository {
    override fun getMessages(chatId: String): Flow<List<Message>> = flow {
        val client = SupabaseClientProvider.client
        val channel = client.realtime.createChannel("omigram-chat-$chatId")
        try {
            client.realtime.connect()
            channel.subscribe()
            emitAll(
                channel.postgresListDataFlow<MessageRow>(
                    schema = "public",
                    table = "messages",
                    primaryKey = PrimaryKey("id") { it.id }
                ) { filter { eq("chat_id", chatId) } }
                    .let { rows ->
                        flow {
                            rows.collect { messageRows ->
                                emit(messageRows.sortedBy { it.createdAt }.map { row ->
                                    row.toMessage(loadProfile(row.senderId))
                                })
                            }
                        }
                    }
            )
        } finally {
            runCatching { channel.unsubscribe() }
        }
    }

    override suspend fun sendMessage(chatId: String, content: String, replyTo: Message?, attachmentUrl: String): Result<Message> = runCatching {
        val userId = currentUserId()
        val inserted = SupabaseClientProvider.client.postgrest.from("messages").insert(
            MessageInsert(chatId, userId, content, if (attachmentUrl.isBlank()) "TEXT" else "IMAGE", attachmentUrl.ifBlank { null }, replyTo?.id)
        ) { select() }.decodeSingle<MessageRow>()
        inserted.toMessage(loadProfile(userId))
    }

    override suspend fun receiveSimulatedMessage(chatId: String, senderUser: User, content: String): Result<Message> =
        Result.failure(IllegalStateException("Simulated messages are disabled in production."))

    override suspend fun deleteMessage(messageId: String): Result<Unit> = runCatching {
        SupabaseClientProvider.client.postgrest.from("messages").delete { filter { eq("id", messageId) } }
    }

    override suspend fun toggleReaction(messageId: String, userId: String, emoji: String): Result<Unit> = runCatching {
        SupabaseClientProvider.client.postgrest.from("message_reactions").upsert(
            mapOf("message_id" to messageId, "user_id" to userId, "emoji" to emoji)
        )
    }

    override suspend fun markMessagesAsRead(chatId: String, currentUserId: String): Result<Unit> = runCatching {
        val ids = SupabaseClientProvider.client.postgrest.from("messages").select {
            filter { eq("chat_id", chatId) }
        }.decodeList<MessageRow>().map { it.id }
        ids.forEach { messageId ->
            SupabaseClientProvider.client.postgrest.from("message_reads").upsert(
                mapOf("message_id" to messageId, "user_id" to currentUserId)
            )
        }
    }

    private suspend fun currentUserId(): String =
        SupabaseClientProvider.client.auth.currentUserOrNull()?.id
            ?: error("A verified Omigram account is required.")

    private suspend fun loadProfile(userId: String): User? = runCatching {
        SupabaseClientProvider.client.postgrest.from("profiles").select {
            filter { eq("id", userId) }
        }.decodeSingle<ProfileRow>().toUser()
    }.getOrNull()
}

class SupabaseLiveChatRepository : ChatRepository {
    override fun getChats(): Flow<List<Chat>> = flow {
        val userId = SupabaseClientProvider.client.auth.currentUserOrNull()?.id ?: return@flow
        val memberships = SupabaseClientProvider.client.postgrest.from("chat_members").select {
            filter { eq("user_id", userId) }
        }.decodeList<ChatMemberRow>()
        emit(memberships.mapNotNull { loadChat(it.chatId, userId) }.sortedByDescending { it.updatedAt })
    }

    override fun getChatById(chatId: String): Flow<Chat?> = flow {
        val userId = SupabaseClientProvider.client.auth.currentUserOrNull()?.id ?: return@flow
        emit(loadChat(chatId, userId))
    }

    override suspend fun getOrCreateChatForUser(contactUser: User): Chat {
        val userId = currentUserId()
        val memberships = SupabaseClientProvider.client.postgrest.from("chat_members").select {
            filter { eq("user_id", userId) }
        }.decodeList<ChatMemberRow>()
        for (membership in memberships) {
            val members = SupabaseClientProvider.client.postgrest.from("chat_members").select {
                filter { eq("chat_id", membership.chatId) }
            }.decodeList<ChatMemberRow>()
            if (members.any { it.userId == contactUser.id }) {
                loadChat(membership.chatId, userId)?.let { return it }
            }
        }
        val chat = SupabaseClientProvider.client.postgrest.from("chats").insert(ChatInsert()) { select() }.decodeSingle<ChatRow>()
        SupabaseClientProvider.client.postgrest.from("chat_members").insert(MembershipInsert(chat.id, userId))
        SupabaseClientProvider.client.postgrest.from("chat_members").insert(MembershipInsert(chat.id, contactUser.id))
        return loadChat(chat.id, userId) ?: error("Could not create chat")
    }

    override suspend fun togglePinChat(chatId: String): Result<Unit> = toggleMembershipFlag(chatId, "is_pinned")
    override suspend fun toggleMuteChat(chatId: String): Result<Unit> = toggleMembershipFlag(chatId, "is_muted")

    override suspend fun deleteChat(chatId: String): Result<Unit> = runCatching {
        val userId = currentUserId()
        SupabaseClientProvider.client.postgrest.from("chat_members").delete {
            filter { eq("chat_id", chatId); eq("user_id", userId) }
        }
    }

    override suspend fun updateLastMessage(chatId: String, message: Message) = Unit

    private suspend fun toggleMembershipFlag(chatId: String, field: String): Result<Unit> = runCatching {
        val userId = currentUserId()
        val membership = SupabaseClientProvider.client.postgrest.from("chat_members").select {
            filter { eq("chat_id", chatId); eq("user_id", userId) }
        }.decodeSingle<ChatMemberRow>()
        val next = if (field == "is_pinned") !membership.isPinned else !membership.isMuted
        SupabaseClientProvider.client.postgrest.from("chat_members").update({ set(field, next) }) {
            filter { eq("chat_id", chatId); eq("user_id", userId) }
        }
    }

    private suspend fun loadChat(chatId: String, currentUserId: String): Chat? = runCatching {
        val row = SupabaseClientProvider.client.postgrest.from("chats").select {
            filter { eq("id", chatId) }
        }.decodeSingle<ChatRow>()
        val members = SupabaseClientProvider.client.postgrest.from("chat_members").select {
            filter { eq("chat_id", chatId) }
        }.decodeList<ChatMemberRow>()
        val users = members.mapNotNull { loadProfile(it.userId) }
        val participant = users.firstOrNull { it.id != currentUserId } ?: users.firstOrNull() ?: return@runCatching null
        val lastMessage = SupabaseClientProvider.client.postgrest.from("messages").select {
            filter { eq("chat_id", chatId) }
            order("created_at", Order.DESCENDING)
            limit(1)
        }.decodeList<MessageRow>().firstOrNull()?.toMessage(loadProfile(participant.id))
        val ownMembership = members.firstOrNull { it.userId == currentUserId }
        Chat(
            id = row.id,
            participant = participant,
            isGroup = row.type == "group",
            groupName = row.title.orEmpty(),
            members = users,
            lastMessage = lastMessage,
            isPinned = ownMembership?.isPinned ?: false,
            isMuted = ownMembership?.isMuted ?: false,
            updatedAt = Instant.parse(row.updatedAt).toEpochMilliseconds(),
            createdAt = Instant.parse(row.createdAt).toEpochMilliseconds()
        )
    }.getOrNull()

    private suspend fun loadProfile(userId: String): User? = runCatching {
        SupabaseClientProvider.client.postgrest.from("profiles").select {
            filter { eq("id", userId) }
        }.decodeSingle<ProfileRow>().toUser()
    }.getOrNull()

    private suspend fun currentUserId(): String =
        SupabaseClientProvider.client.auth.currentUserOrNull()?.id
            ?: error("A verified Omigram account is required.")
}
