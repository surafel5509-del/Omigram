package com.example.data.remote.supabase

import com.example.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

/** Single Supabase client for the Android app. */
object SupabaseClientProvider {
    val isConfigured: Boolean
        get() = BuildConfig.SUPABASE_URL.isNotBlank() &&
            BuildConfig.SUPABASE_PUBLISHABLE_KEY.isNotBlank() &&
            !BuildConfig.SUPABASE_URL.contains("YOUR_PROJECT_REF") &&
            !BuildConfig.SUPABASE_PUBLISHABLE_KEY.contains("YOUR_")

    val client by lazy {
        check(isConfigured) {
            "Supabase is not configured. Add SUPABASE_URL and SUPABASE_PUBLISHABLE_KEY to .env."
        }
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
        ) {
            install(Auth) {
                flowType = io.github.jan.supabase.auth.FlowType.PKCE
                scheme = "omigram"
                host = "auth"
            }
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }
}
