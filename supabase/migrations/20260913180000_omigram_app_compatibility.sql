-- Keep the deployed schema compatible with the Android Supabase repositories.
-- This migration is idempotent and only adds app-facing compatibility columns.

alter table public.chats add column if not exists created_by uuid references auth.users(id) on delete cascade;
alter table public.chats alter column created_by set default auth.uid();
alter table public.chats add column if not exists type text not null default 'direct';

alter table public.chat_members add column if not exists is_muted boolean not null default false;
alter table public.chat_members add column if not exists is_pinned boolean not null default false;

alter table public.messages add column if not exists content text;
alter table public.messages add column if not exists message_type text not null default 'TEXT';
alter table public.messages add column if not exists attachment_url text;
alter table public.messages add column if not exists reply_to_id uuid references public.messages(id) on delete set null;
update public.messages set content = coalesce(content, '') where content is null;
alter table public.messages alter column content set default '';
alter table public.messages alter column content set not null;

alter table public.profiles add column if not exists last_seen timestamptz;
alter table public.message_reactions add column if not exists emoji text;
update public.message_reactions set emoji = coalesce(emoji, '👍') where emoji is null;
alter table public.message_reactions alter column emoji set default '👍';
alter table public.message_reactions alter column emoji set not null;

create index if not exists idx_chats_updated_at on public.chats(updated_at desc);
create index if not exists idx_chat_members_chat on public.chat_members(chat_id);
create index if not exists idx_message_reactions_message_emoji on public.message_reactions(message_id, emoji);

do $$ begin
  begin alter publication supabase_realtime add table public.calls; exception when duplicate_object then null; end;
  begin alter publication supabase_realtime add table public.notifications; exception when duplicate_object then null; end;
end $$;
