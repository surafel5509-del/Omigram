package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.CallStatus
import com.example.core.model.CallType
import com.example.core.model.MessageStatus
import com.example.core.model.MessageType

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val username: String,
    val fullName: String,
    val avatarUrl: String = "",
    val bio: String = "",
    val phone: String = "",
    val email: String = "",
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val participantId: String,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val messageType: MessageType = MessageType.TEXT,
    val content: String,
    val attachmentUrl: String = "",
    val replyToMessageId: String? = null,
    val replyToContent: String? = null,
    val replyToSenderName: String? = null,
    val status: MessageStatus = MessageStatus.SENT,
    val reactionsRaw: String = "", // formatted string e.g. "❤️:user1,🔥:user2"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val id: String,
    val ownerId: String,
    val contactUserId: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "calls")
data class CallEntity(
    @PrimaryKey val id: String,
    val callerId: String,
    val receiverId: String,
    val callType: CallType = CallType.AUDIO,
    val status: CallStatus = CallStatus.INCOMING,
    val durationSeconds: Int = 0,
    val startedAt: Long = System.currentTimeMillis(),
    val endedAt: Long = System.currentTimeMillis()
)
