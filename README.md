# Omigo Chat

**Omigo Chat** is a modern, offline-first Android messaging application designed with Clean Architecture, Jetpack Compose, and Material Design 3.

---

## 📱 Features

- **Private & Direct Messaging**: Real-time reactive messaging with instant delivery and status indicators (Sending, Sent, Delivered, Read).
- **Interactive Conversations**: Quoted reply banners, emoji reactions (👍, ❤️, 😂, 😮, 😢, 🙏), message copying, and simulated realistic contact responses.
- **Rich Media & Attachment Ready**: Attachment sheets for photo, camera, document, and location sharing.
- **Calls Hub**: Audio and video call history with outgoing, incoming, and missed call tracking, plus simulated active call calling dialogs.
- **Contact Management**: Sorted contact directories with alphabetical indexing, search bar filtering, and new contact addition.
- **Personalized Profile**: Full profile management with customizable avatar photo, full name, username, bio, and online presence toggle.
- **Dynamic Theming**: Support for System Default, Light, and Dark themes.

---

## 🛠️ Architecture & Technology Stack

- **UI Layer**: Jetpack Compose, Material 3, Navigation Compose, Coil
- **Presentation Layer**: MVVM, `ViewModel`, Kotlin Coroutines, `StateFlow`, `collectAsStateWithLifecycle`
- **Domain Layer**: Pure Kotlin domain models (`User`, `Chat`, `Message`, `Contact`, `Call`, `Reaction`) and decoupled abstract repository interfaces (`AuthRepository`, `UserRepository`, `ChatRepository`, `MessageRepository`, `ContactRepository`, `CallRepository`, `StorageRepository`).
- **Data Layer (Offline-First)**: Room Persistence Library (`OmigoDatabase`, DAOs, and Entities mapped to future backend schemas).
- **Dependency Injection**: Centralized `AppContainer` providing clean inversion of control.

---

## 🔌 Supabase Migration Guide

Omigo Chat is structured so that **zero UI changes** are required to connect to a live Supabase backend.

### 1. Add Supabase Gradle Dependencies
In `gradle/libs.versions.toml` and `app/build.gradle.kts`, add:
```kotlin
implementation(platform("io.github.jan-tennert.supabase:bom:2.6.0"))
implementation("io.github.jan-tennert.supabase:postgrest-kt")
implementation("io.github.jan-tennert.supabase:gotrue-kt")
implementation("io.github.jan-tennert.supabase:realtime-kt")
implementation("io.github.jan-tennert.supabase:storage-kt")
```

### 2. Configure Supabase Client
In `data/remote/supabase/SupabaseClient.kt`:
```kotlin
val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
    install(Storage)
}
```

### 3. Replace Local Repositories in Dependency Injection
In `com.example.di.DefaultAppContainer`:
```kotlin
// Change from:
override val chatRepository: ChatRepository by lazy { LocalChatRepository(database) }

// To:
override val chatRepository: ChatRepository by lazy { SupabaseChatRepository(supabase) }
```
Because the ViewModels consume the domain interfaces (`ChatRepository`), no UI or ViewModel changes are required.

### 4. Database Schema (PostgreSQL on Supabase)
Omigo Chat entities map directly to the following tables:
- `profiles` (`id uuid references auth.users`, `username text`, `full_name text`, `avatar_url text`, `bio text`, `phone text`, `is_online boolean`, `last_seen bigint`)
- `chats` (`id uuid primary key`, `participant_id uuid references profiles(id)`, `unread_count int`, `is_pinned boolean`, `is_muted boolean`, `created_at bigint`, `updated_at bigint`)
- `messages` (`id uuid primary key`, `chat_id uuid references chats(id)`, `sender_id uuid references profiles(id)`, `message_type text`, `content text`, `attachment_url text`, `reply_to_message_id uuid`, `status text`, `reactions text`, `created_at bigint`)
- `contacts` (`id uuid primary key`, `owner_id uuid references profiles(id)`, `contact_user_id uuid references profiles(id)`)
- `calls` (`id uuid primary key`, `caller_id uuid`, `receiver_id uuid`, `call_type text`, `status text`, `duration_seconds int`, `started_at bigint`)

### 5. Row Level Security (RLS)
Enable RLS on all tables and configure policies ensuring users only read and write their own chats and messages.
