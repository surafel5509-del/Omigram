package com.example.data.local

import com.example.core.model.CallStatus
import com.example.core.model.CallType
import com.example.core.model.Chat
import com.example.core.model.Comment
import com.example.core.model.Highlight
import com.example.core.model.Message
import com.example.core.model.MessageStatus
import com.example.core.model.MessageType
import com.example.core.model.Post
import com.example.core.model.Reel
import com.example.core.model.SocialNotification
import com.example.core.model.SocialNotificationType
import com.example.core.model.Story
import com.example.core.model.User
import com.example.data.local.entity.CallEntity
import com.example.data.local.entity.ChatEntity
import com.example.data.local.entity.ContactEntity
import com.example.data.local.entity.MessageEntity
import com.example.data.local.entity.ProfileEntity

object SampleData {
    const val CURRENT_USER_ID = "user_me"

    val currentUserEntity = ProfileEntity(
        id = CURRENT_USER_ID,
        username = "Michael.Anderson",
        fullName = "Michael Anderson",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80",
        bio = "Product designer who focus on simplicity usability",
        phone = "+1 (555) 890-1234",
        email = "michael.anderson@gmail.com",
        isOnline = true,
        lastSeen = System.currentTimeMillis()
    )

    val currentUser = User(
        id = CURRENT_USER_ID,
        username = "Michael.Anderson",
        fullName = "Michael Anderson",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80",
        bio = "Product designer who focus on simplicity usability",
        phone = "+1 (555) 890-1234",
        email = "michael.anderson@gmail.com",
        website = "https://anderson.design",
        isVerified = true,
        isOnline = true,
        postsCount = 38,
        followersCount = 4200,
        followingCount = 1600,
        joinedDate = "January 2023",
        mutualFollowers = listOf("amberlawes", "sophiac")
    )

    val userMichael = currentUser

    val userJones = User(
        id = "user_jones",
        username = "jones",
        fullName = "Jones",
        avatarUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=300&auto=format&fit=crop&q=80",
        bio = "Fitness, life & minimal design",
        email = "jones@example.com",
        isVerified = false,
        isOnline = true,
        postsCount = 24,
        followersCount = 3200,
        followingCount = 410,
        isFollowing = true
    )

    val userBecker = User(
        id = "user_becker",
        username = "becker",
        fullName = "Becker",
        avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=300&auto=format&fit=crop&q=80",
        bio = "Coffee lover & food explorer ☕",
        email = "becker@example.com",
        isVerified = true,
        isOnline = true,
        postsCount = 48,
        followersCount = 5900,
        followingCount = 350,
        isFollowing = true
    )

    val userBente = User(
        id = "user_bente",
        username = "bente",
        fullName = "Bente",
        avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300&auto=format&fit=crop&q=80",
        bio = "City wanderer & cap enthusiast 🧢",
        email = "bente@example.com",
        isVerified = false,
        isOnline = true,
        postsCount = 31,
        followersCount = 4100,
        followingCount = 290,
        isFollowing = true
    )

    val userAmber = User(
        id = "user_amber",
        username = "amberlawes",
        fullName = "Amber Lawes",
        avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300&auto=format&fit=crop&q=80",
        bio = "Capturing everyday light & minimal architecture ☕",
        email = "amber.lawes@example.com",
        website = "https://amberlawes.photo",
        isVerified = true,
        isOnline = true,
        postsCount = 89,
        followersCount = 14300,
        followingCount = 780,
        isFollowing = true,
        joinedDate = "August 2022",
        mutualFollowers = listOf("Michael.Anderson", "sophiac")
    )

    val userJennifer = User(
        id = "user_jennifer",
        username = "jennifer.h",
        fullName = "Jennifer Harrison",
        avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300&auto=format&fit=crop&q=80",
        bio = "Mother of Maggie and Maral. Creative director & curator.",
        email = "jennifer.h@example.com",
        phone = "+1 (555) 765-4321",
        website = "https://harrisonstudio.com",
        isVerified = false,
        isOnline = false,
        postsCount = 64,
        followersCount = 3100,
        followingCount = 520,
        isFollowing = false,
        joinedDate = "May 2023",
        mutualFollowers = listOf("Michael.Anderson")
    )

    val userSophia = User(
        id = "user_sophia",
        username = "sophiac",
        fullName = "Sophia Chen",
        avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300&auto=format&fit=crop&q=80",
        bio = "Product Designer & design systems lover 🎨",
        email = "sophia.chen@example.com",
        phone = "+1 (555) 234-5678",
        isVerified = true,
        isOnline = true,
        postsCount = 52,
        followersCount = 8900,
        followingCount = 640,
        isFollowing = true,
        joinedDate = "November 2023"
    )

    val userMarcus = User(
        id = "user_marcus",
        username = "marcusv",
        fullName = "Marcus Vance",
        avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300&auto=format&fit=crop&q=80",
        bio = "Engineering Lead • Scaling resilient mobile systems ⚡",
        email = "marcus.vance@example.com",
        phone = "+1 (555) 345-6789",
        isVerified = false,
        isOnline = true,
        postsCount = 29,
        followersCount = 1840,
        followingCount = 390,
        isFollowing = true,
        joinedDate = "July 2023"
    )

    val usersList = listOf(currentUser, userMichael, userAmber, userJennifer, userSophia, userMarcus)

    val users = listOf(
        currentUserEntity,
        ProfileEntity(
            id = userSophia.id,
            username = userSophia.username,
            fullName = userSophia.fullName,
            avatarUrl = userSophia.avatarUrl,
            bio = userSophia.bio,
            phone = userSophia.phone,
            email = userSophia.email,
            isOnline = true,
            lastSeen = System.currentTimeMillis()
        ),
        ProfileEntity(
            id = userMarcus.id,
            username = userMarcus.username,
            fullName = userMarcus.fullName,
            avatarUrl = userMarcus.avatarUrl,
            bio = userMarcus.bio,
            phone = userMarcus.phone,
            email = userMarcus.email,
            isOnline = true,
            lastSeen = System.currentTimeMillis() - 5 * 60 * 1000
        ),
        ProfileEntity(
            id = userMichael.id,
            username = userMichael.username,
            fullName = userMichael.fullName,
            avatarUrl = userMichael.avatarUrl,
            bio = userMichael.bio,
            phone = userMichael.phone,
            email = userMichael.email,
            isOnline = true,
            lastSeen = System.currentTimeMillis()
        ),
        ProfileEntity(
            id = userAmber.id,
            username = userAmber.username,
            fullName = userAmber.fullName,
            avatarUrl = userAmber.avatarUrl,
            bio = userAmber.bio,
            phone = userAmber.phone,
            email = userAmber.email,
            isOnline = true,
            lastSeen = System.currentTimeMillis()
        ),
        ProfileEntity(
            id = userJennifer.id,
            username = userJennifer.username,
            fullName = userJennifer.fullName,
            avatarUrl = userJennifer.avatarUrl,
            bio = userJennifer.bio,
            phone = userJennifer.phone,
            email = userJennifer.email,
            isOnline = false,
            lastSeen = System.currentTimeMillis() - 2 * 3600 * 1000
        )
    )

    val stories = listOf(
        Story(
            id = "story_jones",
            user = userJones,
            mediaUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=500&auto=format&fit=crop&q=80",
            hasUnseenStory = true,
            timeAgo = "14m ago"
        ),
        Story(
            id = "story_becker",
            user = userBecker,
            mediaUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=500&auto=format&fit=crop&q=80",
            hasUnseenStory = true,
            timeAgo = "18m ago"
        ),
        Story(
            id = "story_bente",
            user = userBente,
            mediaUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop&q=80",
            hasUnseenStory = true,
            timeAgo = "44m ago"
        ),
        Story(
            id = "story_amber",
            user = userAmber,
            mediaUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500&auto=format&fit=crop&q=80",
            hasUnseenStory = true,
            timeAgo = "1h ago"
        ),
        Story(
            id = "story_jennifer",
            user = userJennifer,
            mediaUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=500&auto=format&fit=crop&q=80",
            hasUnseenStory = false,
            timeAgo = "3h ago"
        )
    )

    val posts = listOf(
        Post(
            id = "post_1",
            author = userAmber,
            imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80",
            caption = "Some days are for planning. Some days are for building. Today is for both.",
            location = "San Francisco, CA",
            likesCount = 1420,
            commentsCount = 86,
            timeAgo = "1d",
            isLiked = false,
            isBookmarked = false,
            comments = listOf(
                Comment(id = "c_1", user = userMichael, text = "Love this framing!", createdAt = "18h ago", likesCount = 12),
                Comment(id = "c_2", user = userSophia, text = "Totally agree, great energy!", createdAt = "12h ago", likesCount = 4)
            )
        ),
        Post(
            id = "post_2",
            author = userMichael,
            imageUrl = "https://images.unsplash.com/photo-1512436991641-6745cdb1723f?w=800&auto=format&fit=crop&q=80",
            caption = "Design is not just what it looks like and feels like. Design is how it works. Studio setup refresh.",
            location = "Brooklyn, NY",
            likesCount = 2890,
            commentsCount = 142,
            timeAgo = "2d",
            isLiked = true,
            isBookmarked = true,
            comments = listOf(
                Comment(id = "c_3", user = userAmber, text = "Clean desk goals right there.", createdAt = "1d ago", likesCount = 8)
            )
        ),
        Post(
            id = "post_3",
            author = userSophia,
            imageUrl = "https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?w=800&auto=format&fit=crop&q=80",
            caption = "Golden hour walks through the coast. Taking time off screens to recharge.",
            location = "Big Sur, California",
            likesCount = 3780,
            commentsCount = 210,
            timeAgo = "3d",
            isLiked = false,
            isBookmarked = false
        ),
        Post(
            id = "post_4",
            author = userJennifer,
            imageUrl = "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?w=800&auto=format&fit=crop&q=80",
            caption = "Ceramics workshop morning. Crafting tactile shapes and calm textures.",
            location = "Kyoto, Japan",
            likesCount = 940,
            commentsCount = 45,
            timeAgo = "4d",
            isLiked = false
        )
    )

    val reels = listOf(
        Reel(
            id = "reel_1",
            creator = userAmber,
            videoThumbnailUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80",
            caption = "A day in the life of a product designer in SF #design #aesthetic #vlog",
            audioTrackTitle = "Calm Beats • Amber's Mix",
            likesCount = 24600,
            commentsCount = 1420,
            sharesCount = 580,
            isLiked = true
        ),
        Reel(
            id = "reel_2",
            creator = userMichael,
            videoThumbnailUrl = "https://images.unsplash.com/photo-1512436991641-6745cdb1723f?w=800&auto=format&fit=crop&q=80",
            caption = "Prototyping 60fps micro-interactions with Kotlin Compose 🚀",
            audioTrackTitle = "Electronic Synthwave - Studio",
            likesCount = 18900,
            commentsCount = 940,
            sharesCount = 310
        ),
        Reel(
            id = "reel_3",
            creator = userSophia,
            videoThumbnailUrl = "https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?w=800&auto=format&fit=crop&q=80",
            caption = "Morning espresso routine before team design sync ☕✨",
            audioTrackTitle = "Coffee & Lo-Fi Lounge",
            likesCount = 31200,
            commentsCount = 2100,
            sharesCount = 890
        )
    )

    val highlights = listOf(
        Highlight(id = "h1", title = "Studio", coverUrl = "https://images.unsplash.com/photo-1512436991641-6745cdb1723f?w=200&auto=format&fit=crop&q=80"),
        Highlight(id = "h2", title = "Design", coverUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80"),
        Highlight(id = "h3", title = "Travel", coverUrl = "https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?w=200&auto=format&fit=crop&q=80"),
        Highlight(id = "h4", title = "Setup", coverUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&auto=format&fit=crop&q=80")
    )

    val socialNotifications = listOf(
        SocialNotification(
            id = "n1",
            actor = userMichael,
            type = SocialNotificationType.LIKE,
            postPreviewUrl = posts[0].imageUrl,
            timeAgo = "15m ago"
        ),
        SocialNotification(
            id = "n2",
            actor = userAmber,
            type = SocialNotificationType.COMMENT,
            commentText = "Clean desk goals right there.",
            postPreviewUrl = posts[1].imageUrl,
            timeAgo = "1h ago"
        ),
        SocialNotification(
            id = "n3",
            actor = userJennifer,
            type = SocialNotificationType.FOLLOW,
            timeAgo = "3h ago",
            isFollowingBack = false
        ),
        SocialNotification(
            id = "n4",
            actor = userSophia,
            type = SocialNotificationType.LIKE,
            postPreviewUrl = posts[1].imageUrl,
            timeAgo = "5h ago"
        ),
        SocialNotification(
            id = "n5",
            actor = userMarcus,
            type = SocialNotificationType.FOLLOW,
            timeAgo = "1d ago",
            isFollowingBack = true
        )
    )

    val chats = listOf(
        ChatEntity(
            id = "chat_sophia",
            participantId = "user_sophia",
            unreadCount = 2,
            isPinned = true,
            isMuted = false,
            updatedAt = System.currentTimeMillis() - 2 * 60 * 1000
        ),
        ChatEntity(
            id = "chat_michael",
            participantId = "user_michael",
            unreadCount = 1,
            isPinned = true,
            isMuted = false,
            updatedAt = System.currentTimeMillis() - 15 * 60 * 1000
        ),
        ChatEntity(
            id = "chat_amber",
            participantId = "user_amber",
            unreadCount = 0,
            isPinned = true,
            isMuted = false,
            updatedAt = System.currentTimeMillis() - 45 * 60 * 1000
        ),
        ChatEntity(
            id = "chat_marcus",
            participantId = "user_marcus",
            unreadCount = 0,
            isPinned = false,
            isMuted = false,
            updatedAt = System.currentTimeMillis() - 2 * 3600 * 1000
        ),
        ChatEntity(
            id = "chat_jennifer",
            participantId = "user_jennifer",
            unreadCount = 0,
            isPinned = false,
            isMuted = false,
            updatedAt = System.currentTimeMillis() - 24 * 3600 * 1000
        )
    )

    val now = System.currentTimeMillis()

    val messages = listOf(
        MessageEntity(
            id = "msg_s1",
            chatId = "chat_sophia",
            senderId = CURRENT_USER_ID,
            messageType = MessageType.TEXT,
            content = "Hey Sophia! Have you checked out the new light mode design system for Omigram?",
            status = MessageStatus.READ,
            createdAt = now - 30 * 60 * 1000
        ),
        MessageEntity(
            id = "msg_s2",
            chatId = "chat_sophia",
            senderId = "user_sophia",
            messageType = MessageType.TEXT,
            content = "Yes! The blue accent #0095F6 and clean white layout feel so snappy and refined.",
            status = MessageStatus.READ,
            reactionsRaw = "❤️:user_me",
            createdAt = now - 22 * 60 * 1000
        ),
        MessageEntity(
            id = "msg_s3",
            chatId = "chat_sophia",
            senderId = CURRENT_USER_ID,
            messageType = MessageType.TEXT,
            content = "Awesome! The story tray and 3-step onboarding flow are ready as well.",
            status = MessageStatus.READ,
            createdAt = now - 15 * 60 * 1000
        ),
        MessageEntity(
            id = "msg_s4",
            chatId = "chat_sophia",
            senderId = "user_sophia",
            messageType = MessageType.TEXT,
            content = "Let's review the final build together today! ☕✨",
            status = MessageStatus.DELIVERED,
            reactionsRaw = "👍:user_me",
            createdAt = now - 2 * 60 * 1000
        ),
        MessageEntity(
            id = "msg_m1",
            chatId = "chat_michael",
            senderId = "user_michael",
            messageType = MessageType.TEXT,
            content = "Hey Alex, just shared the updated 3-column photo grid spec. Looks pixel perfect!",
            status = MessageStatus.READ,
            createdAt = now - 25 * 60 * 1000
        ),
        MessageEntity(
            id = "msg_m2",
            chatId = "chat_michael",
            senderId = CURRENT_USER_ID,
            messageType = MessageType.TEXT,
            content = "Thanks Michael! The verified badge and followers stats look great on your profile too.",
            status = MessageStatus.READ,
            createdAt = now - 18 * 60 * 1000
        ),
        MessageEntity(
            id = "msg_m3",
            chatId = "chat_michael",
            senderId = "user_michael",
            messageType = MessageType.TEXT,
            content = "Agreed! The floating pill navigation bar is super smooth.",
            status = MessageStatus.DELIVERED,
            createdAt = now - 15 * 60 * 1000
        )
    )

    val contacts = listOf(
        ContactEntity(id = "c1", ownerId = CURRENT_USER_ID, contactUserId = userSophia.id),
        ContactEntity(id = "c2", ownerId = CURRENT_USER_ID, contactUserId = userMichael.id),
        ContactEntity(id = "c3", ownerId = CURRENT_USER_ID, contactUserId = userAmber.id),
        ContactEntity(id = "c4", ownerId = CURRENT_USER_ID, contactUserId = userMarcus.id),
        ContactEntity(id = "c5", ownerId = CURRENT_USER_ID, contactUserId = userJennifer.id)
    )

    val calls = listOf(
        CallEntity(
            id = "call_1",
            callerId = userSophia.id,
            receiverId = CURRENT_USER_ID,
            callType = CallType.VIDEO,
            status = CallStatus.INCOMING,
            durationSeconds = 480,
            startedAt = now - 40 * 60 * 1000
        ),
        CallEntity(
            id = "call_2",
            callerId = CURRENT_USER_ID,
            receiverId = userMichael.id,
            callType = CallType.AUDIO,
            status = CallStatus.OUTGOING,
            durationSeconds = 195,
            startedAt = now - 4 * 3600 * 1000
        )
    )

    val samplePosts: List<Post> get() = posts
    val sampleStories: List<Story> get() = stories
    val sampleReels: List<Reel> get() = reels
    val sampleNotifications: List<SocialNotification> get() = socialNotifications
    val sampleUsers: List<User> get() = usersList
    val sampleComments: List<Comment> get() = posts.flatMap { it.comments }

    val sampleChats: List<Chat> get() = listOf(
        Chat(
            id = "chat_sophia",
            participant = userSophia,
            lastMessage = Message(
                id = "m_s4",
                chatId = "chat_sophia",
                senderId = "user_sophia",
                content = "Let's review the final build together today! ☕✨",
                status = MessageStatus.DELIVERED,
                createdAt = System.currentTimeMillis() - 2 * 60 * 1000
            ),
            unreadCount = 2,
            isPinned = true
        ),
        Chat(
            id = "chat_michael",
            participant = userMichael,
            lastMessage = Message(
                id = "m_m3",
                chatId = "chat_michael",
                senderId = "user_michael",
                content = "Agreed! The floating pill navigation bar is super smooth.",
                status = MessageStatus.DELIVERED,
                createdAt = System.currentTimeMillis() - 15 * 60 * 1000
            ),
            unreadCount = 1,
            isPinned = true
        ),
        Chat(
            id = "chat_amber",
            participant = userAmber,
            lastMessage = Message(
                id = "m_a1",
                chatId = "chat_amber",
                senderId = "user_amber",
                content = "Shared a new story! Check out the golden hour shots.",
                status = MessageStatus.READ,
                createdAt = System.currentTimeMillis() - 45 * 60 * 1000
            ),
            unreadCount = 0,
            isPinned = true
        ),
        Chat(
            id = "chat_marcus",
            participant = userMarcus,
            lastMessage = Message(
                id = "m_ma1",
                chatId = "chat_marcus",
                senderId = "user_marcus",
                content = "All build checks are passing!",
                status = MessageStatus.READ,
                createdAt = System.currentTimeMillis() - 2 * 3600 * 1000
            ),
            unreadCount = 0
        ),
        Chat(
            id = "chat_jennifer",
            participant = userJennifer,
            lastMessage = Message(
                id = "m_j1",
                chatId = "chat_jennifer",
                senderId = "user_jennifer",
                content = "Thanks for the feedback on the photo gallery!",
                status = MessageStatus.READ,
                createdAt = System.currentTimeMillis() - 24 * 3600 * 1000
            ),
            unreadCount = 0
        )
    )
}
