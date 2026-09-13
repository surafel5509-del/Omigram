package com.example.presentation.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.SampleData
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText
import com.example.ui.theme.ReceivedBubbleGray
import com.example.ui.theme.ReceivedBubbleText
import com.example.ui.theme.SentBubbleBlue
import com.example.ui.theme.SentBubbleText
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
    // 18dp rounded corners as requested
    val bubbleShape = RoundedCornerShape(18.dp)

    val bubbleBg = if (isCurrentUser) SentBubbleBlue else ReceivedBubbleGray
    val contentColor = if (isCurrentUser) SentBubbleText else ReceivedBubbleText
    val timeString = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.createdAt))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 3.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = bubbleShape,
            color = bubbleBg,
            modifier = Modifier
                .widthIn(min = 60.dp, max = 290.dp)
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
                                if (isCurrentUser) Color.Black.copy(alpha = 0.15f)
                                else Color.White
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Column {
                            Text(
                                text = message.replyToSenderName ?: "Reply",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrentUser) Color.White else OmigramAccentBlue
                            )
                            Text(
                                text = message.replyToContent,
                                fontSize = 12.sp,
                                maxLines = 1,
                                color = if (isCurrentUser) Color.White.copy(alpha = 0.85f) else OmigramSecondaryText
                            )
                        }
                    }
                }

                // Message Text Content
                Text(
                    text = message.content,
                    fontSize = 15.sp,
                    color = contentColor,
                    lineHeight = 20.sp
                )
            }
        }

        // Timestamp below bubbles in small gray text as strictly specified
        Row(
            modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = timeString,
                fontSize = 11.sp,
                color = OmigramSecondaryText
            )
            if (isCurrentUser) {
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = if (message.status == MessageStatus.READ) Icons.Default.DoneAll else Icons.Default.Done,
                    contentDescription = "Delivered",
                    tint = if (message.status == MessageStatus.READ) OmigramAccentBlue else OmigramSecondaryText,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        // Reactions display
        if (message.reactions.isNotEmpty()) {
            val reactionCounts = message.reactions.groupBy { it.reaction }
            Row(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .testTag("reactions_row_${message.id}"),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                reactionCounts.forEach { (emoji, list) ->
                    val hasUserReacted = list.any { it.userId == SampleData.CURRENT_USER_ID }
                    Surface(
                        shape = CircleShape,
                        color = if (hasUserReacted) Color(0xFFE0F2FE) else OmigramSecondaryBackground,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onReactionClick(emoji) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = emoji, fontSize = 12.sp)
                            if (list.size > 1) {
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = list.size.toString(),
                                    fontSize = 11.sp,
                                    color = OmigramSecondaryText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateSeparator(dateText: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = OmigramSecondaryBackground,
            modifier = Modifier.testTag("date_separator")
        ) {
            Text(
                text = dateText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = OmigramSecondaryText,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}
