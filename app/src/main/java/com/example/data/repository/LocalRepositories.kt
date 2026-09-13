package com.example.data.repository

import com.example.core.model.Call
import com.example.core.model.Chat
import com.example.core.model.Contact
import com.example.core.model.Message
import com.example.core.model.MessageStatus
import com.example.core.model.MessageType
import com.example.core.model.Reaction
import com.example.core.model.User
import com.example.data.local.OmigoDatabase
import com.example.data.local.SampleData
import com.example.data.local.entity.ChatEntity
import com.example.data.local.entity.ContactEntity
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.CallRepository
import com.example.domain.repository.ChatRepository
import com.example.domain.repository.ContactRepository
import com.example.domain.repository.MessageRepository
import com.example.domain.repository.StorageRepository
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class LocalAuthRepository(
    private val database: OmigoDatabase
) : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: Flow<User?> = _currentUser

    override suspend fun getCurrentUser(): User? {
        return _currentUser.value
    }

    override suspend fun login(email: String, password: String): Result<User> {
        delay(500)
        val cleanIdentifier = email.trim()
        if (cleanIdentifier.isBlank()) {
            return Result.failure(IllegalArgumentException("Please enter your email or username"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        val allProfiles = database.profileDao().getProfilesList()
        val matched = allProfiles.firstOrNull {
            it.email.equals(cleanIdentifier, ignoreCase = true) ||
            it.username.equals(cleanIdentifier.removePrefix("@"), ignoreCase = true)
        }

        if (matched != null) {
            val user = matched.toDomain().copy(isOnline = true)
            database.profileDao().insertProfile(user.toEntity())
            _currentUser.value = user
            return Result.success(user)
        }

        return Result.failure(IllegalArgumentException("No account found for '$cleanIdentifier'. Please register first."))
    }

    override suspend fun register(fullName: String, username: String, email: String, password: String): Result<User> {
        delay(600)
        val cleanName = fullName.trim()
        val cleanUsername = username.trim().lowercase().removePrefix("@")
        val cleanEmail = email.trim()

        if (cleanName.isBlank() || cleanUsername.isBlank() || cleanEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("All fields are required"))
        }
        if (cleanUsername.length < 3) {
            return Result.failure(IllegalArgumentException("Username must be at least 3 characters"))
        }
        if (!cleanEmail.contains("@") || !cleanEmail.contains(".")) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        val allProfiles = database.profileDao().getProfilesList()
        if (allProfiles.any { it.username.equals(cleanUsername, ignoreCase = true) }) {
            return Result.failure(IllegalArgumentException("Username @$cleanUsername is already taken"))
        }
        if (allProfiles.any { it.email.equals(cleanEmail, ignoreCase = true) }) {
            return Result.failure(IllegalArgumentException("An account with this email already exists"))
        }

        val userId = UUID.randomUUID().toString()
        val newUser = User(
            id = userId,
            username = cleanUsername,
            fullName = cleanName,
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=300&auto=format&fit=crop&q=80",
            email = cleanEmail,
            bio = "Hey there! I am using Omigram.",
            isOnline = true,
            postsCount = 0,
            followersCount = 0,
            followingCount = 0,
            joinedDate = "Just now"
        )
        database.profileDao().insertProfile(newUser.toEntity())
        _currentUser.value = newUser
        return Result.success(newUser)
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        delay(500)
        val cleanEmail = email.trim()
        if (!cleanEmail.contains("@")) {
            return Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }
        val allProfiles = database.profileDao().getProfilesList()
        val exists = allProfiles.any { it.email.equals(cleanEmail, ignoreCase = true) }
        if (!exists) {
            return Result.failure(IllegalArgumentException("No account registered with this email"))
        }
        return Result.success(Unit)
    }

    override suspend fun logout(): Result<Unit> {
        _currentUser.value = null
        return Result.success(Unit)
    }

    override suspend fun isAuthenticated(): Boolean {
        return _currentUser.value != null
    }
}

class LocalUserRepository(
    private val database: OmigoDatabase
) : UserRepository {

    override fun observeUser(userId: String): Flow<User?> {
        return database.profileDao().observeProfile(userId).map { it?.toDomain() }
    }

    override suspend fun getUser(userId: String): User? {
        return database.profileDao().getProfile(userId)?.toDomain()
    }

    override suspend fun updateProfile(user: User): Result<User> {
        database.profileDao().updateProfile(user.toEntity())
        return Result.success(user)
    }

    override suspend fun searchUsers(query: String): List<User> {
        return database.profileDao().searchProfiles(query).map { it.toDomain() }
    }

    override suspend fun setOnlineStatus(userId: String, isOnline: Boolean): Result<Unit> {
        database.profileDao().updateOnlineStatus(userId, isOnline, System.currentTimeMillis())
        return Result.success(Unit)
    }
}

class LocalChatRepository(
    private val database: OmigoDatabase
) : ChatRepository {

    override fun getChats(): Flow<List<Chat>> {
        return combine(
            database.chatDao().getChats(),
            database.profileDao().getAllProfiles()
        ) { chats, profiles ->
            val profileMap = profiles.associateBy { it.id }
            chats.mapNotNull { chatEntity ->
                val participant = profileMap[chatEntity.participantId]?.toDomain() ?: return@mapNotNull null
                val lastMsgEntity = database.messageDao().getLastMessageForChat(chatEntity.id)
                chatEntity.toDomain(participant, lastMsgEntity?.toDomain())
            }
        }
    }

    override fun getChatById(chatId: String): Flow<Chat?> {
        return database.chatDao().getChatById(chatId).map { chatEntity ->
            if (chatEntity == null) return@map null
            val participantEntity = database.profileDao().getProfile(chatEntity.participantId)
                ?: return@map null
            val lastMsg = database.messageDao().getLastMessageForChat(chatId)?.toDomain()
            chatEntity.toDomain(participantEntity.toDomain(), lastMsg)
        }
    }

    override suspend fun getOrCreateChatForUser(contactUser: User): Chat {
        val existing = database.chatDao().findChatByParticipant(contactUser.id)
        return if (existing != null) {
            val lastMsg = database.messageDao().getLastMessageForChat(existing.id)?.toDomain()
            existing.toDomain(contactUser, lastMsg)
        } else {
            val newChat = ChatEntity(
                id = "chat_${contactUser.id}",
                participantId = contactUser.id,
                unreadCount = 0,
                isPinned = false,
                isMuted = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            database.chatDao().insertChat(newChat)
            newChat.toDomain(contactUser, null)
        }
    }

    override suspend fun togglePinChat(chatId: String): Result<Unit> {
        database.chatDao().togglePin(chatId)
        return Result.success(Unit)
    }

    override suspend fun toggleMuteChat(chatId: String): Result<Unit> {
        database.chatDao().toggleMute(chatId)
        return Result.success(Unit)
    }

    override suspend fun deleteChat(chatId: String): Result<Unit> {
        database.chatDao().deleteChat(chatId)
        return Result.success(Unit)
    }

    override suspend fun updateLastMessage(chatId: String, message: Message) {
        database.chatDao().updateChatTimestamp(chatId, message.createdAt)
    }
}

class LocalMessageRepository(
    private val database: OmigoDatabase,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : MessageRepository {

    override fun getMessages(chatId: String): Flow<List<Message>> {
        return database.messageDao().getMessagesForChat(chatId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun sendMessage(
        chatId: String,
        content: String,
        replyTo: Message?,
        attachmentUrl: String
    ): Result<Message> {
        val newMsg = Message(
            id = "msg_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}",
            chatId = chatId,
            senderId = SampleData.CURRENT_USER_ID,
            messageType = if (attachmentUrl.isNotBlank()) MessageType.IMAGE else MessageType.TEXT,
            content = content,
            attachmentUrl = attachmentUrl,
            replyToMessageId = replyTo?.id,
            replyToContent = replyTo?.content,
            replyToSenderName = if (replyTo != null) {
                if (replyTo.senderId == SampleData.CURRENT_USER_ID) "You" else "Contact"
            } else null,
            status = MessageStatus.SENT,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        database.messageDao().insertMessage(newMsg.toEntity())
        database.chatDao().updateChatTimestamp(chatId, newMsg.createdAt)

        // Simulate intelligent realistic contact reply after 2.5 seconds to make conversation interactive!
        simulateContactReply(chatId, content)

        return Result.success(newMsg)
    }

    override suspend fun receiveSimulatedMessage(
        chatId: String,
        senderUser: User,
        content: String
    ): Result<Message> {
        val newMsg = Message(
            id = "msg_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}",
            chatId = chatId,
            senderId = senderUser.id,
            messageType = MessageType.TEXT,
            content = content,
            status = MessageStatus.DELIVERED,
            createdAt = System.currentTimeMillis()
        )
        database.messageDao().insertMessage(newMsg.toEntity())
        database.chatDao().updateChatTimestamp(chatId, newMsg.createdAt)
        return Result.success(newMsg)
    }

    private fun simulateContactReply(chatId: String, userMessage: String) {
        scope.launch {
            delay(2200)
            val chatEntity = database.chatDao().getChatById(chatId).firstOrNull() ?: return@launch
            val contact = database.profileDao().getProfile(chatEntity.participantId) ?: return@launch

            val replies = listOf(
                "Got it! That sounds great.",
                "Thanks for the update, checking this now! 👍",
                "Sounds like a plan! Let's follow up soon.",
                "Awesome! Everything looks crisp and clean on my side.",
                "Nice work! Let me review this and get back to you in a bit."
            )
            val replyText = replies.random()
            receiveSimulatedMessage(chatId, contact.toDomain(), replyText)
        }
    }

    override suspend fun deleteMessage(messageId: String): Result<Unit> {
        database.messageDao().markDeleted(messageId)
        return Result.success(Unit)
    }

    override suspend fun toggleReaction(messageId: String, userId: String, emoji: String): Result<Unit> {
        val msg = database.messageDao().getMessageById(messageId) ?: return Result.failure(Exception("Message not found"))
        val domainMsg = msg.toDomain()
        val currentReactions = domainMsg.reactions.toMutableList()
        val existingIndex = currentReactions.indexOfFirst { it.userId == userId && it.reaction == emoji }
        if (existingIndex >= 0) {
            currentReactions.removeAt(existingIndex)
        } else {
            currentReactions.add(Reaction(messageId = messageId, userId = userId, reaction = emoji))
        }
        val raw = currentReactions.joinToString(",") { "${it.reaction}:${it.userId}" }
        database.messageDao().updateReactions(messageId, raw)
        return Result.success(Unit)
    }

    override suspend fun markMessagesAsRead(chatId: String, currentUserId: String): Result<Unit> {
        database.messageDao().markMessagesAsRead(chatId, currentUserId)
        database.chatDao().clearUnread(chatId)
        return Result.success(Unit)
    }
}

class LocalContactRepository(
    private val database: OmigoDatabase
) : ContactRepository {

    override fun getContacts(): Flow<List<Contact>> {
        return combine(
            database.contactDao().getContacts(SampleData.CURRENT_USER_ID),
            database.profileDao().getAllProfiles()
        ) { contacts, profiles ->
            val profileMap = profiles.associateBy { it.id }
            contacts.mapNotNull { contactEntity ->
                val user = profileMap[contactEntity.contactUserId]?.toDomain() ?: return@mapNotNull null
                contactEntity.toDomain(user)
            }
        }
    }

    override suspend fun addContact(usernameOrEmail: String): Result<Contact> {
        val query = usernameOrEmail.trim().lowercase().removePrefix("@")
        val found = database.profileDao().searchProfiles(query).firstOrNull()
        return if (found != null) {
            val contact = ContactEntity(
                id = "c_${System.currentTimeMillis()}",
                ownerId = SampleData.CURRENT_USER_ID,
                contactUserId = found.id
            )
            database.contactDao().insertContact(contact)
            Result.success(contact.toDomain(found.toDomain()))
        } else {
            // Create a new contact profile entry
            val newUserId = "user_${UUID.randomUUID().toString().take(6)}"
            val newProfile = User(
                id = newUserId,
                username = query,
                fullName = query.replaceFirstChar { it.uppercase() },
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                bio = "Available on Omigo Chat",
                email = "$query@example.com",
                isOnline = true
            )
            database.profileDao().insertProfile(newProfile.toEntity())
            val contact = ContactEntity(
                id = "c_${System.currentTimeMillis()}",
                ownerId = SampleData.CURRENT_USER_ID,
                contactUserId = newProfile.id
            )
            database.contactDao().insertContact(contact)
            Result.success(contact.toDomain(newProfile))
        }
    }

    override suspend fun deleteContact(contactId: String): Result<Unit> {
        database.contactDao().deleteContact(contactId)
        return Result.success(Unit)
    }

    override suspend fun searchContacts(query: String): List<Contact> {
        val profiles = database.profileDao().searchProfiles(query).map { it.toDomain() }
        return profiles.map { Contact(ownerId = SampleData.CURRENT_USER_ID, contactUser = it) }
    }
}

class LocalCallRepository(
    private val database: OmigoDatabase
) : CallRepository {

    override fun getCallHistory(): Flow<List<Call>> {
        return combine(
            database.callDao().getCallHistory(),
            database.profileDao().getAllProfiles()
        ) { calls, profiles ->
            val profileMap = profiles.associateBy { it.id }
            calls.mapNotNull { callEntity ->
                val caller = profileMap[callEntity.callerId]?.toDomain() ?: return@mapNotNull null
                val receiver = profileMap[callEntity.receiverId]?.toDomain() ?: return@mapNotNull null
                callEntity.toDomain(caller, receiver)
            }
        }
    }

    override suspend fun recordCall(call: Call): Result<Unit> {
        val entity = com.example.data.local.entity.CallEntity(
            id = call.id,
            callerId = call.caller.id,
            receiverId = call.receiver.id,
            callType = call.callType,
            status = call.status,
            durationSeconds = call.durationSeconds,
            startedAt = call.startedAt,
            endedAt = call.endedAt
        )
        database.callDao().insertCall(entity)
        return Result.success(Unit)
    }

    override suspend fun clearHistory(): Result<Unit> {
        database.callDao().clearCallHistory()
        return Result.success(Unit)
    }
}

class LocalStorageRepository : StorageRepository {
    override suspend fun uploadAvatar(uri: String): Result<String> {
        delay(400)
        return Result.success(uri)
    }

    override suspend fun uploadMedia(uri: String, type: String): Result<String> {
        delay(500)
        return Result.success(uri)
    }
}
