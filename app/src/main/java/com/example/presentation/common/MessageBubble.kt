package com.example.presentation.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.Message
import com.example.core.model.MessageStatus
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialPillDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: Message,
    isCurrentUser: Boolean,
    onLongClick: () -> Unit,
    onReactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Distinct editorial bubble shapes:
    // Asymmetric corners giving a tailored, modern card silhouette
    val bubbleShape = if (isCurrentUser) {
        RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 4.dp)
    }

    val bubbleBg = if (isCurrentUser) Color(0xFF1E2430) else Color.White
    val contentColor = if (isCurrentUser) Color.White else Color(0xFF1A1A1A)
    val timeString = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.createdAt))

    val isVoiceNote = message.content.startsWith("🎤") || message.content.contains("Voice note")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = bubbleShape,
            color = bubbleBg,
            border = if (!isCurrentUser) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)) else null,
            shadowElevation = if (!isCurrentUser) 2.dp else 3.dp,
            modifier = Modifier
                .widthIn(min = 70.dp, max = 300.dp)
                .clip(bubbleShape)
                .combinedClickable(
                    onClick = { /* normal tap */ },
                    onLongClick = onLongClick
                )
                .testTag("message_bubble_${message.id}")
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Quoted reply view if present
                if (!message.replyToContent.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isCurrentUser) Color.White.copy(alpha = 0.12f)
                                else Color(0xFFF4F5F7)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Column {
                            Text(
                                text = message.replyToSenderName ?: "Reply",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrentUser) Color(0xFF93C5FD) else SocialBrandBlue
                            )
                            Text(
                                text = message.replyToContent,
                                fontSize = 12.sp,
                                maxLines = 1,
                                color = if (isCurrentUser) Color.White.copy(alpha = 0.85f) else Color(0xFF6B7280)
                            )
                        }
                    }
                }

                // Voice Note or Regular Content
                if (isVoiceNote) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isCurrentUser) Color.White.copy(alpha = 0.2f) else SocialBrandBlue.copy(alpha = 0.15f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Play voice note",
                                    tint = if (isCurrentUser) Color.White else SocialBrandBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        // Waveform simulation bars
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val barHeights = listOf(8, 14, 22, 12, 18, 24, 16, 20, 10, 14, 6)
                            barHeights.forEach { h ->
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(h.dp)
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(
                                            if (isCurrentUser) Color.White.copy(alpha = 0.7f)
                                            else SocialBrandBlue.copy(alpha = 0.6f)
                                        )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "0:14",
                            fontSize = 11.sp,
                            color = if (isCurrentUser) Color.White.copy(alpha = 0.7f) else Color(0xFF6B7280)
                        )
                    }
                } else {
                    // Regular Message Content
                    Text(
                        text = message.content,
                        fontSize = 14.5.sp,
                        color = contentColor,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Timestamp below bubbles in small clean text
        Row(
            modifier = Modifier.padding(top = 3.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = timeString,
                fontSize = 11.sp,
                color = Color(0xFF8E8E93)
            )
            if (isCurrentUser) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (message.status == MessageStatus.READ) Icons.Default.DoneAll else Icons.Default.Done,
                    contentDescription = "Delivered",
                    tint = if (message.status == MessageStatus.READ) SocialBrandBlue else Color(0xFF8E8E93),
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Reactions display
        if (message.reactions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(12.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                message.reactions.forEach { reaction ->
                    Text(
                        text = reaction.reaction,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onReactionClick(reaction.reaction) }
                    )
                }
            }
        }
    }
}
