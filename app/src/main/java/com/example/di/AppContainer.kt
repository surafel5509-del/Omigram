package com.example.di

import android.content.Context
import com.example.data.local.OmigoDatabase
import com.example.data.remote.supabase.SupabaseLiveChatRepository
import com.example.data.remote.supabase.SupabaseLiveMessageRepository
import com.example.data.remote.supabase.SupabaseStrictAuthRepository
import com.example.data.remote.supabase.SupabaseUserRepository
import com.example.data.repository.LocalCallRepository
import com.example.data.repository.LocalContactRepository
import com.example.data.repository.LocalStorageRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.CallRepository
import com.example.domain.repository.ChatRepository
import com.example.domain.repository.ContactRepository
import com.example.domain.repository.MessageRepository
import com.example.domain.repository.StorageRepository
import com.example.domain.repository.UserRepository

interface AppContainer {
    val authRepository: AuthRepository
    val userRepository: UserRepository
    val chatRepository: ChatRepository
    val messageRepository: MessageRepository
    val contactRepository: ContactRepository
    val callRepository: CallRepository
    val storageRepository: StorageRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database: OmigoDatabase by lazy { OmigoDatabase.getDatabase(context) }

    override val authRepository: AuthRepository by lazy { SupabaseStrictAuthRepository() }
    override val userRepository: UserRepository by lazy { SupabaseUserRepository() }
    override val chatRepository: ChatRepository by lazy { SupabaseLiveChatRepository() }
    override val messageRepository: MessageRepository by lazy { SupabaseLiveMessageRepository() }
    override val contactRepository: ContactRepository by lazy { LocalContactRepository(database) }
    override val callRepository: CallRepository by lazy { LocalCallRepository(database) }
    override val storageRepository: StorageRepository by lazy { LocalStorageRepository() }
}
