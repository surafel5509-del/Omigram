# Omigram + Supabase

This repository is prepared for a dedicated Supabase project. The current Android default remains the local Room repositories so the app can still run without backend credentials.

## 1. Create a dedicated Supabase project

Do not reuse an unrelated project. Create a new project for Omigram, then copy the project URL and **publishable key** from the Connect/API Keys area.

Never put a secret/service-role key in the Android app.

## 2. Configure local secrets

Copy `.env.example` to `.env` and fill in:

```text
SUPABASE_URL=https://YOUR_PROJECT_REF.supabase.co
SUPABASE_PUBLISHABLE_KEY=sb_publishable_...
```

`.env` is ignored by Git.

## 3. Apply the database migration

Run:

```text
supabase/migrations/20260913000100_omigram_core.sql
```

The migration creates:

- profiles
- chats
- chat_members
- messages
- message_reads
- message_reactions
- contacts
- calls
- notifications
- private `omigram-media` Storage bucket
- Auth profile trigger
- indexes
- RLS policies
- initial Realtime publication entries

## 4. Configure Auth redirects

The Android client uses the native deep link:

```text
omigram://auth
```

Add that exact URL to the Supabase Auth redirect URL allow-list before using OAuth/recovery flows.

The client is configured for PKCE.

## 5. Android SDK

The app uses `supabase-kt` 3.8.0 and Ktor 3.5.2. The Supabase client is centralized in:

```text
app/src/main/java/com/example/data/remote/supabase/SupabaseClientProvider.kt
```

The client installs Auth, PostgREST, Realtime and Storage.

## 6. Architecture

The intended production path is:

```text
Compose
  -> ViewModel
  -> domain repository interface
  -> remote Supabase repository
  -> Supabase Auth / Postgres / Realtime / Storage
```

Room remains the local cache/offline source. The next backend phase should implement synchronization instead of making Compose talk directly to Supabase.

## 7. Realtime

The first implementation can use Postgres Changes for simplicity. Supabase currently recommends Broadcast for higher-scale realtime systems, so the message stream can later move to private Broadcast topics without changing the UI/domain contracts.

## 8. Security checklist

Before production:

- keep RLS enabled on every user-owned table
- verify chat membership in every message policy
- never ship a service-role/secret key
- use private Storage for private media
- configure production SMTP for Auth emails
- add pagination to message queries
- replace local/sample authentication and simulated replies
- add Room migrations instead of destructive migration
- add server-side validation for usernames and message payloads
- test RLS with multiple users
