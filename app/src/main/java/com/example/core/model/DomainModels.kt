package com.example.core.model

import java.util.UUID

enum class MessageType {
    TEXT,
    IMAGE,
    VOICE,
    DOCUMENT
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ
}

enum class CallType {
    AUDIO,
    VIDEO
}

enum class CallStatus {
    INCOMING,
    OUTGOING,
    MISSED
}

data class User(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val fullName: String,
    val avatarUrl: String = "",
    val bio: String = "",
    val phone: String = "",
    val email: String = "",
    val website: String = "",
    val isVerified: Boolean = false,
    val isOnline: Boolean = false,
    val postsCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean = false,
    val joinedDate: String = "September 2024",
    val mutualFollowers: List<String> = emptyList(),
    val lastSeen: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class Reaction(
    val id: String = UUID.randomUUID().toString(),
    val messageId: String,
    val userId: String,
    val reaction: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class Attachment(
    val id: String = UUID.randomUUID().toString(),
    val url: String,
    val name: String,
    val sizeBytes: Long = 0,
    val mimeType: String = ""
)

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val chatId: String,
    val senderId: String,
    val messageType: MessageType = MessageType.TEXT,
    val content: String,
    val attachmentUrl: String = "",
    val replyToMessageId: String? = null,
    val replyToContent: String? = null,
    val replyToSenderName: String? = null,
    val status: MessageStatus = MessageStatus.SENT,
    val reactions: List<Reaction> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)

data class Chat(
    val id: String = UUID.randomUUID().toString(),
    val participant: User,
    val lastMessage: Message? = null,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isTyping: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class Contact(
    val id: String = UUID.randomUUID().toString(),
    val ownerId: String,
    val contactUser: User,
    val createdAt: Long = System.currentTimeMillis()
)

data class Call(
    val id: String = UUID.randomUUID().toString(),
    val caller: User,
    val receiver: User,
    val callType: CallType = CallType.AUDIO,
    val status: CallStatus = CallStatus.INCOMING,
    val durationSeconds: Int = 0,
    val startedAt: Long = System.currentTimeMillis(),
    val endedAt: Long = System.currentTimeMillis()
)

data class Comment(
    val id: String = UUID.randomUUID().toString(),
    val user: User,
    val text: String,
    val createdAt: String = "2h ago",
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class Post(
    val id: String = UUID.randomUUID().toString(),
    val author: User,
    val imageUrl: String,
    val caption: String,
    val location: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val timeAgo: String = "2h ago",
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val comments: List<Comment> = emptyList()
)

data class Story(
    val id: String = UUID.randomUUID().toString(),
    val user: User,
    val mediaUrl: String,
    val hasUnseenStory: Boolean = true,
    val isLive: Boolean = false,
    val timeAgo: String = "44m ago"
)

data class Reel(
    val id: String = UUID.randomUUID().toString(),
    val creator: User,
    val videoThumbnailUrl: String,
    val caption: String,
    val audioTrackTitle: String = "Original audio - Omigram",
    val likesCount: Int = 12400,
    val commentsCount: Int = 890,
    val sharesCount: Int = 340,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false
)

data class Highlight(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val coverUrl: String
)

enum class SocialNotificationType {
    LIKE,
    COMMENT,
    FOLLOW
}

data class SocialNotification(
    val id: String = UUID.randomUUID().toString(),
    val actor: User,
    val type: SocialNotificationType,
    val postPreviewUrl: String? = null,
    val commentText: String? = null,
    val timeAgo: String = "1h ago",
    val isFollowingBack: Boolean = false
)

data class NotificationItem(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val type: String,
    val title: String,
    val body: String,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
