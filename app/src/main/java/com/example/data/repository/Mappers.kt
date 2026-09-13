package com.example.data.repository

import com.example.core.model.Call
import com.example.core.model.Chat
import com.example.core.model.Contact
import com.example.core.model.Message
import com.example.core.model.Reaction
import com.example.core.model.User
import com.example.data.local.entity.CallEntity
import com.example.data.local.entity.ChatEntity
import com.example.data.local.entity.ContactEntity
import com.example.data.local.entity.MessageEntity
import com.example.data.local.entity.ProfileEntity

fun ProfileEntity.toDomain(): User = User(
    id = id,
    username = username,
    fullName = fullName,
    avatarUrl = avatarUrl,
    bio = bio,
    phone = phone,
    email = email,
    isOnline = isOnline,
    lastSeen = lastSeen,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun User.toEntity(): ProfileEntity = ProfileEntity(
    id = id,
    username = username,
    fullName = fullName,
    avatarUrl = avatarUrl,
    bio = bio,
    phone = phone,
    email = email,
    isOnline = isOnline,
    lastSeen = lastSeen,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun MessageEntity.toDomain(): Message {
    val reactionsList = if (reactionsRaw.isBlank()) {
        emptyList()
    } else {
        reactionsRaw.split(",").mapNotNull { entry ->
            val parts = entry.split(":")
            if (parts.size == 2) {
                Reaction(
                    messageId = id,
                    reaction = parts[0],
                    userId = parts[1]
                )
            } else null
        }
    }
    return Message(
        id = id,
        chatId = chatId,
        senderId = senderId,
        messageType = messageType,
        content = content,
        attachmentUrl = attachmentUrl,
        replyToMessageId = replyToMessageId,
        replyToContent = replyToContent,
        replyToSenderName = replyToSenderName,
        status = status,
        reactions = reactionsList,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}

fun Message.toEntity(): MessageEntity {
    val reactionsRawString = reactions.joinToString(",") { "${it.reaction}:${it.userId}" }
    return MessageEntity(
        id = id,
        chatId = chatId,
        senderId = senderId,
        messageType = messageType,
        content = content,
        attachmentUrl = attachmentUrl,
        replyToMessageId = replyToMessageId,
        replyToContent = replyToContent,
        replyToSenderName = replyToSenderName,
        status = status,
        reactionsRaw = reactionsRawString,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt
    )
}

fun ChatEntity.toDomain(participant: User, lastMessage: Message? = null): Chat = Chat(
    id = id,
    participant = participant,
    lastMessage = lastMessage,
    unreadCount = unreadCount,
    isPinned = isPinned,
    isMuted = isMuted,
    isTyping = false,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ContactEntity.toDomain(contactUser: User): Contact = Contact(
    id = id,
    ownerId = ownerId,
    contactUser = contactUser,
    createdAt = createdAt
)

fun CallEntity.toDomain(caller: User, receiver: User): Call = Call(
    id = id,
    caller = caller,
    receiver = receiver,
    callType = callType,
    status = status,
    durationSeconds = durationSeconds,
    startedAt = startedAt,
    endedAt = endedAt
)
