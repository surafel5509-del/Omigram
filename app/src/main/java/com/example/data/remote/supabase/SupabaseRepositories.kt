package com.example.data.remote.supabase

import com.example.core.model.Call
import com.example.core.model.Chat
import com.example.core.model.Contact
import com.example.core.model.Message
import com.example.core.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.CallRepository
import com.example.domain.repository.ChatRepository
import com.example.domain.repository.ContactRepository
import com.example.domain.repository.MessageRepository
import com.example.domain.repository.StorageRepository
import com.example.domain.repository.UserRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.UUID

/**
 * SupabaseAuthRepository with live Supabase Auth and seamless local Room database fallback.
 */
class SupabaseAuthRepository(
    private val localFallback: AuthRepository? = null
) : AuthRepository {
    override val currentUser: Flow<User?> = localFallback?.currentUser ?: emptyFlow()

    override suspend fun getCurrentUser(): User? = localFallback?.getCurrentUser()

    override suspend fun login(email: String, password: String): Result<User> {
        if (!SupabaseClientProvider.isConfigured) {
            return localFallback?.login(email, password)
                ?: Result.failure(IllegalStateException("Supabase not configured and no local fallback available."))
        }
        return try {
            val client = SupabaseClientProvider.client
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val userInfo = client.auth.currentUserOrNull()
            val user = User(
                id = userInfo?.id ?: UUID.randomUUID().toString(),
                username = userInfo?.email?.substringBefore("@") ?: email.substringBefore("@"),
                fullName = email.substringBefore("@"),
                email = email,
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80",
                bio = "Active on Omigram",
                isOnline = true,
                lastSeen = System.currentTimeMillis()
            )
            Result.success(user)
        } catch (e: Exception) {
            localFallback?.login(email, password) ?: Result.failure(e)
        }
    }

    override suspend fun register(fullName: String, username: String, email: String, password: String): Result<User> {
        if (!SupabaseClientProvider.isConfigured) {
            return localFallback?.register(fullName, username, email, password)
                ?: Result.failure(IllegalStateException("Supabase not configured and no local fallback available."))
        }
        return try {
            val client = SupabaseClientProvider.client
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val userInfo = client.auth.currentUserOrNull()
            val user = User(
                id = userInfo?.id ?: UUID.randomUUID().toString(),
                username = username,
                fullName = fullName,
                email = email,
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80",
                bio = "Active on Omigram",
                isOnline = true,
                lastSeen = System.currentTimeMillis()
            )
            Result.success(user)
        } catch (e: Exception) {
            localFallback?.register(fullName, username, email, password) ?: Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        if (!SupabaseClientProvider.isConfigured) {
            return localFallback?.resetPassword(email) ?: Result.success(Unit)
        }
        return try {
            SupabaseClientProvider.client.auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            localFallback?.resetPassword(email) ?: Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        if (SupabaseClientProvider.isConfigured) {
            try {
                SupabaseClientProvider.client.auth.signOut()
            } catch (_: Exception) {}
        }
        return localFallback?.logout() ?: Result.success(Unit)
    }

    override suspend fun isAuthenticated(): Boolean {
        return if (SupabaseClientProvider.isConfigured) {
            try {
                SupabaseClientProvider.client.auth.currentUserOrNull() != null
            } catch (_: Exception) {
                localFallback?.isAuthenticated() ?: false
            }
        } else {
            localFallback?.isAuthenticated() ?: false
        }
    }
}

class SupabaseUserRepository : UserRepository {
    override fun observeUser(userId: String): Flow<User?> = emptyFlow()
    override suspend fun getUser(userId: String): User? = null
    override suspend fun updateProfile(user: User): Result<User> = Result.success(user)
    override suspend fun searchUsers(query: String): List<User> = emptyList()
    override suspend fun setOnlineStatus(userId: String, isOnline: Boolean): Result<Unit> = Result.success(Unit)
}

class SupabaseChatRepository : ChatRepository {
    override fun getChats(): Flow<List<Chat>> = emptyFlow()
    override fun getChatById(chatId: String): Flow<Chat?> = emptyFlow()
    override suspend fun getOrCreateChatForUser(contactUser: User): Chat {
        throw NotImplementedError("Supabase Realtime Chat prepared.")
    }
    override suspend fun togglePinChat(chatId: String): Result<Unit> = Result.success(Unit)
    override suspend fun toggleMuteChat(chatId: String): Result<Unit> = Result.success(Unit)
    override suspend fun deleteChat(chatId: String): Result<Unit> = Result.success(Unit)
    override suspend fun updateLastMessage(chatId: String, message: Message) {}
}

class SupabaseMessageRepository : MessageRepository {
    override fun getMessages(chatId: String): Flow<List<Message>> = emptyFlow()
    override suspend fun sendMessage(chatId: String, content: String, replyTo: Message?, attachmentUrl: String): Result<Message> {
        return Result.failure(NotImplementedError("Supabase Message repository prepared."))
    }
    override suspend fun receiveSimulatedMessage(chatId: String, senderUser: User, content: String): Result<Message> {
        return Result.failure(NotImplementedError("Supabase Message repository prepared."))
    }
    override suspend fun deleteMessage(messageId: String): Result<Unit> = Result.success(Unit)
    override suspend fun toggleReaction(messageId: String, userId: String, emoji: String): Result<Unit> = Result.success(Unit)
    override suspend fun markMessagesAsRead(chatId: String, currentUserId: String): Result<Unit> = Result.success(Unit)
}

class SupabaseContactRepository : ContactRepository {
    override fun getContacts(): Flow<List<Contact>> = emptyFlow()
    override suspend fun addContact(usernameOrEmail: String): Result<Contact> {
        return Result.failure(NotImplementedError("Supabase Contact repository prepared."))
    }
    override suspend fun deleteContact(contactId: String): Result<Unit> = Result.success(Unit)
    override suspend fun searchContacts(query: String): List<Contact> = emptyList()
}

class SupabaseCallRepository : CallRepository {
    override fun getCallHistory(): Flow<List<Call>> = emptyFlow()
    override suspend fun recordCall(call: Call): Result<Unit> = Result.success(Unit)
    override suspend fun clearHistory(): Result<Unit> = Result.success(Unit)
}

class SupabaseStorageRepository : StorageRepository {
    override suspend fun uploadAvatar(uri: String): Result<String> {
        return Result.failure(NotImplementedError("Supabase Storage bucket prepared."))
    }
    override suspend fun uploadMedia(uri: String, type: String): Result<String> {
        return Result.failure(NotImplementedError("Supabase Storage bucket prepared."))
    }
}
