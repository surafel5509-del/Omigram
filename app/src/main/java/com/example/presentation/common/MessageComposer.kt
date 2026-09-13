package com.example.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.Message
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText

@Composable
fun MessageComposer(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onAttachmentClick: () -> Unit,
    onEmojiClick: () -> Unit,
    onMicClick: () -> Unit,
    replyToMessage: Message? = null,
    onCancelReply: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        color = OmigramBackground,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 0.5.dp, color = OmigramBorder)
            .testTag("message_composer")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            // Replying banner
            AnimatedVisibility(visible = replyToMessage != null) {
                if (replyToMessage != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(OmigramSecondaryBackground)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(28.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(OmigramAccentBlue)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Replying to ${replyToMessage.replyToSenderName ?: "message"}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OmigramAccentBlue
                            )
                            Text(
                                text = replyToMessage.content,
                                fontSize = 12.sp,
                                color = OmigramSecondaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(
                            onClick = onCancelReply,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel reply",
                                tint = OmigramSecondaryText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Input bar with rounded pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Camera icon
                IconButton(
                    onClick = onAttachmentClick,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        tint = OmigramPrimaryText,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Rounded pill text field
                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    placeholder = {
                        Text(
                            text = "Message...",
                            fontSize = 14.sp,
                            color = OmigramSecondaryText
                        )
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = onEmojiClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SentimentSatisfiedAlt,
                                contentDescription = "Emoji picker",
                                tint = OmigramSecondaryText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = onAttachmentClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Gallery",
                                tint = OmigramSecondaryText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(999.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = OmigramSecondaryBackground,
                        unfocusedContainerColor = OmigramSecondaryBackground,
                        focusedBorderColor = OmigramBorder,
                        unfocusedBorderColor = OmigramBorder,
                        focusedTextColor = OmigramPrimaryText,
                        unfocusedTextColor = OmigramPrimaryText
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("composer_text_input")
                )

                Spacer(modifier = Modifier.width(6.dp))

                if (text.trim().isNotEmpty()) {
                    // Blue "Send" button when text entered
                    Button(
                        onClick = onSend,
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OmigramAccentBlue),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("composer_send_button")
                    ) {
                        Text(
                            text = "Send",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    // Mic button
                    IconButton(
                        onClick = onMicClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("composer_mic_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Microphone",
                            tint = OmigramPrimaryText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}
