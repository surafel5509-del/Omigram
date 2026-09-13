-- Omigram core schema
-- Run this migration against a dedicated Omigram Supabase project.
-- The Android app must use only the project's publishable key.

create extension if not exists pgcrypto;

create table if not exists public.profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  username text not null unique,
  full_name text not null default '',
  bio text not null default '',
  avatar_url text,
  is_online boolean not null default false,
  last_seen_at timestamptz,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  constraint profiles_username_length check (char_length(username) between 3 and 30)
);

create table if not exists public.chats (
  id uuid primary key default gen_random_uuid(),
  type text not null default 'direct' check (type in ('direct', 'group')),
  title text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.chat_members (
  chat_id uuid not null references public.chats(id) on delete cascade,
  user_id uuid not null references public.profiles(id) on delete cascade,
  joined_at timestamptz not null default now(),
  last_read_at timestamptz,
  is_muted boolean not null default false,
  is_pinned boolean not null default false,
  primary key (chat_id, user_id)
);

create table if not exists public.messages (
  id uuid primary key default gen_random_uuid(),
  chat_id uuid not null references public.chats(id) on delete cascade,
  sender_id uuid not null references public.profiles(id) on delete cascade,
  content text not null default '',
  message_type text not null default 'TEXT' check (message_type in ('TEXT', 'IMAGE', 'VIDEO', 'AUDIO', 'DOCUMENT', 'VOICE')),
  attachment_url text,
  reply_to_id uuid references public.messages(id) on delete set null,
  created_at timestamptz not null default now(),
  edited_at timestamptz,
  deleted_at timestamptz
);

create table if not exists public.message_reads (
  message_id uuid not null references public.messages(id) on delete cascade,
  user_id uuid not null references public.profiles(id) on delete cascade,
  read_at timestamptz not null default now(),
  primary key (message_id, user_id)
);

create table if not exists public.message_reactions (
  message_id uuid not null references public.messages(id) on delete cascade,
  user_id uuid not null references public.profiles(id) on delete cascade,
  emoji text not null,
  created_at timestamptz not null default now(),
  primary key (message_id, user_id, emoji)
);

create table if not exists public.contacts (
  user_id uuid not null references public.profiles(id) on delete cascade,
  contact_user_id uuid not null references public.profiles(id) on delete cascade,
  created_at timestamptz not null default now(),
  primary key (user_id, contact_user_id),
  constraint contacts_not_self check (user_id <> contact_user_id)
);

create table if not exists public.calls (
  id uuid primary key default gen_random_uuid(),
  caller_id uuid not null references public.profiles(id) on delete cascade,
  callee_id uuid not null references public.profiles(id) on delete cascade,
  call_type text not null check (call_type in ('VOICE', 'VIDEO')),
  status text not null check (status in ('MISSED', 'COMPLETED', 'DECLINED', 'CANCELLED')),
  started_at timestamptz,
  ended_at timestamptz,
  created_at timestamptz not null default now()
);

create table if not exists public.notifications (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references public.profiles(id) on delete cascade,
  actor_id uuid references public.profiles(id) on delete set null,
  type text not null,
  payload jsonb not null default '{}'::jsonb,
  read_at timestamptz,
  created_at timestamptz not null default now()
);

create index if not exists chat_members_user_idx on public.chat_members(user_id);
create index if not exists messages_chat_created_idx on public.messages(chat_id, created_at desc);
create index if not exists messages_sender_idx on public.messages(sender_id);
create index if not exists message_reads_user_idx on public.message_reads(user_id);
create index if not exists notifications_user_created_idx on public.notifications(user_id, created_at desc);

-- Profile creation from Supabase Auth.
create or replace function public.handle_new_user()
returns trigger
language plpgsql
security definer set search_path = public
as $$
begin
  insert into public.profiles (id, username, full_name)
  values (
    new.id,
    coalesce(nullif(new.raw_user_meta_data ->> 'username', ''), 'user_' || substr(new.id::text, 1, 8)),
    coalesce(new.raw_user_meta_data ->> 'full_name', '')
  )
  on conflict (id) do nothing;
  return new;
end;
$$;

drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created
after insert on auth.users
for each row execute function public.handle_new_user();

-- Keep updated_at server-controlled.
create or replace function public.set_updated_at()
returns trigger
language plpgsql
as $$
begin
  new.updated_at = now();
  return new;
end;
$$;

drop trigger if exists profiles_set_updated_at on public.profiles;
create trigger profiles_set_updated_at
before update on public.profiles
for each row execute function public.set_updated_at();

drop trigger if exists chats_set_updated_at on public.chats;
create trigger chats_set_updated_at
before update on public.chats
for each row execute function public.set_updated_at();

-- RLS baseline: every private resource is accessible only to an authenticated
-- user who is entitled to that row.
alter table public.profiles enable row level security;
alter table public.chats enable row level security;
alter table public.chat_members enable row level security;
alter table public.messages enable row level security;
alter table public.message_reads enable row level security;
alter table public.message_reactions enable row level security;
alter table public.contacts enable row level security;
alter table public.calls enable row level security;
alter table public.notifications enable row level security;

create policy "profiles are readable by authenticated users"
on public.profiles for select to authenticated using (true);

create policy "users can update their own profile"
on public.profiles for update to authenticated
using (id = auth.uid()) with check (id = auth.uid());

create policy "users can read chats they belong to"
on public.chats for select to authenticated
using (exists (select 1 from public.chat_members m where m.chat_id = id and m.user_id = auth.uid()));

create policy "authenticated users can create chats"
on public.chats for insert to authenticated
with check (true);

create policy "users can read their memberships"
on public.chat_members for select to authenticated
using (user_id = auth.uid());

create policy "users can create memberships for themselves"
on public.chat_members for insert to authenticated
with check (user_id = auth.uid());

create policy "users can update their own membership"
on public.chat_members for update to authenticated
using (user_id = auth.uid()) with check (user_id = auth.uid());

create policy "chat members can read messages"
on public.messages for select to authenticated
using (exists (select 1 from public.chat_members m where m.chat_id = messages.chat_id and m.user_id = auth.uid()));

create policy "chat members can send messages as themselves"
on public.messages for insert to authenticated
with check (
  sender_id = auth.uid()
  and exists (select 1 from public.chat_members m where m.chat_id = messages.chat_id and m.user_id = auth.uid())
);

create policy "senders can edit their messages"
on public.messages for update to authenticated
using (sender_id = auth.uid()) with check (sender_id = auth.uid());

create policy "senders can delete their messages"
on public.messages for delete to authenticated
using (sender_id = auth.uid());

create policy "users can manage their message reads"
on public.message_reads for all to authenticated
using (user_id = auth.uid()) with check (user_id = auth.uid());

create policy "chat members can read reactions"
on public.message_reactions for select to authenticated
using (exists (
  select 1 from public.messages msg
  join public.chat_members cm on cm.chat_id = msg.chat_id
  where msg.id = message_reactions.message_id and cm.user_id = auth.uid()
));

create policy "users can manage their own reactions"
on public.message_reactions for insert to authenticated
with check (user_id = auth.uid());
create policy "users can delete their own reactions"
on public.message_reactions for delete to authenticated
using (user_id = auth.uid());

create policy "users can read their contacts"
on public.contacts for select to authenticated using (user_id = auth.uid());
create policy "users can add their contacts"
on public.contacts for insert to authenticated with check (user_id = auth.uid());
create policy "users can delete their contacts"
on public.contacts for delete to authenticated using (user_id = auth.uid());

create policy "users can read calls they participate in"
on public.calls for select to authenticated
using (caller_id = auth.uid() or callee_id = auth.uid());
create policy "users can create calls they participate in"
on public.calls for insert to authenticated
with check (caller_id = auth.uid() or callee_id = auth.uid());

create policy "users can read their notifications"
on public.notifications for select to authenticated using (user_id = auth.uid());
create policy "users can update their notifications"
on public.notifications for update to authenticated
using (user_id = auth.uid()) with check (user_id = auth.uid());

-- Realtime for the first production phase. Broadcast can replace this for very high scale.
alter publication supabase_realtime add table public.messages;
alter publication supabase_realtime add table public.message_reactions;
alter publication supabase_realtime add table public.message_reads;
alter publication supabase_realtime add table public.chat_members;

-- Private media bucket. Files should be addressed by authenticated user ownership
-- rather than using a public bucket.
insert into storage.buckets (id, name, public)
values ('omigram-media', 'omigram-media', false)
on conflict (id) do nothing;

create policy "authenticated users can read Omigram media"
on storage.objects for select to authenticated
using (bucket_id = 'omigram-media');

create policy "users can upload into their own media folder"
on storage.objects for insert to authenticated
with check (bucket_id = 'omigram-media' and (storage.foldername(name))[1] = auth.uid()::text);

create policy "users can update their own media"
on storage.objects for update to authenticated
using (bucket_id = 'omigram-media' and owner_id = auth.uid()::text)
with check (bucket_id = 'omigram-media' and owner_id = auth.uid()::text);

create policy "users can delete their own media"
on storage.objects for delete to authenticated
using (bucket_id = 'omigram-media' and owner_id = auth.uid()::text);
