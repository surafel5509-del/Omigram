-- Omigram chat creation needs the authenticated chat creator to add the other participant.
-- The creator is still restricted to chats they already belong to.
drop policy if exists "users can create memberships for themselves" on public.chat_members;
create policy "users can create memberships for themselves or their chat"
on public.chat_members
for insert to authenticated
with check (
  user_id = auth.uid()
  or exists (
    select 1
    from public.chat_members existing_member
    where existing_member.chat_id = chat_members.chat_id
      and existing_member.user_id = auth.uid()
  )
);
