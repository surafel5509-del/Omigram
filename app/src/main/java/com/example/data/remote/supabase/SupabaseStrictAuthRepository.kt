package com.example.data.remote.supabase

import com.example.core.model.User
import com.example.domain.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseStrictAuthRepository : AuthRepository {
    override val currentUser: Flow<User?> = flow { emit(getCurrentUser()) }

    override suspend fun getCurrentUser(): User? {
        if (!SupabaseClientProvider.isConfigured) return null
        val authUser = SupabaseClientProvider.client.auth.currentUserOrNull() ?: return null
        if (authUser.emailConfirmedAt == null || SupabaseClientProvider.client.auth.currentSessionOrNull() == null) {
            runCatching { SupabaseClientProvider.client.auth.signOut() }
            return null
        }
        return authUser.toDomain()
    }

    override suspend fun login(email: String, password: String): Result<User> {
        if (!SupabaseClientProvider.isConfigured) return Result.failure(IllegalStateException("Supabase is not configured."))
        return try {
            val client = SupabaseClientProvider.client
            client.auth.signInWith(Email) {
                this.email = email.trim()
                this.password = password
            }
            val user = client.auth.currentUserOrNull()
                ?: return Result.failure(IllegalStateException("No authenticated user was returned."))
            if (user.emailConfirmedAt == null || client.auth.currentSessionOrNull() == null) {
                client.auth.signOut()
                return Result.failure(IllegalStateException("Please verify your email address before signing in to Omigram."))
            }
            Result.success(user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(fullName: String, username: String, email: String, password: String): Result<User> {
        if (!SupabaseClientProvider.isConfigured) return Result.failure(IllegalStateException("Supabase is not configured."))
        return try {
            val client = SupabaseClientProvider.client
            client.auth.signUpWith(Email) {
                this.email = email.trim()
                this.password = password
                data = buildJsonObject {
                    put("username", username.trim())
                    put("full_name", fullName.trim())
                }
            }
            val user = client.auth.currentUserOrNull()
                ?: return Result.failure(IllegalStateException("Account creation failed."))
            if (user.emailConfirmedAt == null || client.auth.currentSessionOrNull() == null) {
                runCatching { client.auth.signOut() }
                return Result.failure(OmigramEmailVerificationRequiredException(email.trim()))
            }
            Result.success(user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        check(SupabaseClientProvider.isConfigured) { "Supabase is not configured." }
        SupabaseClientProvider.client.auth.resetPasswordForEmail(email.trim())
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        if (SupabaseClientProvider.isConfigured) SupabaseClientProvider.client.auth.signOut()
    }

    override suspend fun isAuthenticated(): Boolean = getCurrentUser() != null

    private suspend fun io.github.jan.supabase.auth.user.User.toDomain(): User {
        val profile = runCatching {
            SupabaseClientProvider.client.postgrest.from("profiles").select {
                filter { eq("id", id) }
            }.decodeSingle<ProfileRow>()
        }.getOrNull()
        return User(
            id = id,
            username = profile?.username ?: email?.substringBefore("@") ?: "user",
            fullName = profile?.fullName ?: email?.substringBefore("@") ?: "Omigram User",
            email = email.orEmpty(),
            avatarUrl = profile?.avatarUrl.orEmpty(),
            bio = profile?.bio.orEmpty(),
            isVerified = emailConfirmedAt != null,
            isOnline = profile?.isOnline ?: true,
            lastSeen = System.currentTimeMillis()
        )
    }

    @Serializable
    private data class ProfileRow(
        val id: String,
        val username: String,
        @SerialName("full_name") val fullName: String,
        val bio: String = "",
        @SerialName("avatar_url") val avatarUrl: String? = null,
        @SerialName("is_online") val isOnline: Boolean = false
    )
}

class OmigramEmailVerificationRequiredException(email: String) :
    Exception("We sent a verification email to $email. Verify it before opening Omigram.")
