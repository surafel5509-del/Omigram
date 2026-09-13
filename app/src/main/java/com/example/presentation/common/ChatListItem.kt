package com.example.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.Chat
import com.example.core.model.MessageStatus
import com.example.data.local.SampleData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatListItem(
    chat: Chat,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxWidth()
            .testTag("chat_item_${chat.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                imageUrl = chat.participant.avatarUrl,
                name = chat.participant.fullName,
                size = 52.dp,
                isOnline = chat.participant.isOnline,
                showOnlineBadge = true
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.participant.fullName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    val timeText = formatMessageTime(chat.lastMessage?.createdAt ?: chat.updatedAt)
                    Text(
                        text = timeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (chat.unreadCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.padding(top = 4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val lastMsg = chat.lastMessage
                        if (lastMsg != null && lastMsg.senderId == SampleData.CURRENT_USER_ID) {
                            MessageStatusIcon(
                                status = lastMsg.status,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }

                        val snippet = when {
                            chat.isTyping -> "Typing..."
                            lastMsg != null -> lastMsg.content
                            else -> "No messages yet"
                        }

                        Text(
                            text = snippet,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (chat.unreadCount > 0) FontWeight.Medium else FontWeight.Normal
                            ),
                            color = if (chat.isTyping) MaterialTheme.colorScheme.primary
                            else if (chat.unreadCount > 0) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (chat.isMuted) {
                            Icon(
                                imageVector = Icons.Default.VolumeOff,
                                contentDescription = "Muted",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        if (chat.isPinned) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pinned",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        if (chat.unreadCount > 0) {
                            UnreadBadge(count = chat.unreadCount)
                        }
                    }
                }
            }
        }
    }
}

private fun formatMessageTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val oneDay = 24 * 3600 * 1000L

    return when {
        diff < oneDay -> SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
        diff < 2 * oneDay -> "Yesterday"
        diff < 7 * oneDay -> SimpleDateFormat("EEE", Locale.getDefault()).format(Date(timestamp))
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}
