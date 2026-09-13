package com.example.domain.repository

import com.example.core.model.Call
import com.example.core.model.Chat
import com.example.core.model.Contact
import com.example.core.model.Message
import com.example.core.model.Reaction
import com.example.core.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun getCurrentUser(): User?
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(fullName: String, username: String, email: String, password: String): Result<User>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun isAuthenticated(): Boolean
}

interface UserRepository {
    fun observeUser(userId: String): Flow<User?>
    suspend fun getUser(userId: String): User?
    suspend fun updateProfile(user: User): Result<User>
    suspend fun searchUsers(query: String): List<User>
    suspend fun setOnlineStatus(userId: String, isOnline: Boolean): Result<Unit>
}

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    fun getChatById(chatId: String): Flow<Chat?>
    suspend fun getOrCreateChatForUser(contactUser: User): Chat
    suspend fun togglePinChat(chatId: String): Result<Unit>
    suspend fun toggleMuteChat(chatId: String): Result<Unit>
    suspend fun deleteChat(chatId: String): Result<Unit>
    suspend fun updateLastMessage(chatId: String, message: Message)
}

interface MessageRepository {
    fun getMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, content: String, replyTo: Message? = null, attachmentUrl: String = ""): Result<Message>
    suspend fun receiveSimulatedMessage(chatId: String, senderUser: User, content: String): Result<Message>
    suspend fun deleteMessage(messageId: String): Result<Unit>
    suspend fun toggleReaction(messageId: String, userId: String, emoji: String): Result<Unit>
    suspend fun markMessagesAsRead(chatId: String, currentUserId: String): Result<Unit>
}

interface ContactRepository {
    fun getContacts(): Flow<List<Contact>>
    suspend fun addContact(usernameOrEmail: String): Result<Contact>
    suspend fun deleteContact(contactId: String): Result<Unit>
    suspend fun searchContacts(query: String): List<Contact>
}

interface CallRepository {
    fun getCallHistory(): Flow<List<Call>>
    suspend fun recordCall(call: Call): Result<Unit>
    suspend fun clearHistory(): Result<Unit>
}

interface StorageRepository {
    suspend fun uploadAvatar(uri: String): Result<String>
    suspend fun uploadMedia(uri: String, type: String): Result<String>
}
