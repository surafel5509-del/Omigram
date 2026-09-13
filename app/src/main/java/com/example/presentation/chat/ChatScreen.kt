package com.example.presentation.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.core.model.User
import com.example.data.local.SampleData
import com.example.presentation.common.DateSeparator
import com.example.presentation.common.MessageBubble
import com.example.presentation.common.MessageComposer
import com.example.presentation.common.TypingIndicator
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramErrorRed
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText
import com.example.ui.theme.OnlineGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onNavigateBack: () -> Unit,
    onStartCall: (participant: User, isVideo: Boolean) -> Unit = { _, _ -> },
    onNavigateToUserProfile: (userId: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val chat by viewModel.chat.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(OmigramBackground)
            .testTag("chat_screen"),
        topBar = {
            TopAppBar(
                title = {
                    val participant = chat?.participant
                    if (participant != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onNavigateToUserProfile(participant.id) }
                        ) {
                            Box(modifier = Modifier.size(38.dp)) {
                                AsyncImage(
                                    model = participant.avatarUrl,
                                    contentDescription = participant.fullName,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .border(1.dp, OmigramBorder, CircleShape)
                                )
                                if (participant.isOnline) {
                                    Box(
                                        modifier = Modifier
                                            .size(11.dp)
                                            .clip(CircleShape)
                                            .background(OnlineGreen)
                                            .border(1.5.dp, OmigramBackground, CircleShape)
                                            .align(Alignment.BottomEnd)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = participant.fullName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OmigramPrimaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (participant.isOnline) "Active now" else "Active recently",
                                    fontSize = 11.sp,
                                    color = if (participant.isOnline) OnlineGreen else OmigramSecondaryText
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("chat_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OmigramPrimaryText
                        )
                    }
                },
                actions = {
                    val participant = chat?.participant
                    IconButton(
                        onClick = {
                            if (participant != null) onStartCall(participant, false)
                        },
                        modifier = Modifier.testTag("chat_voice_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Audio call",
                            tint = OmigramPrimaryText
                        )
                    }

                    IconButton(
                        onClick = {
                            if (participant != null) onStartCall(participant, true)
                        },
                        modifier = Modifier.testTag("chat_video_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video call",
                            tint = OmigramPrimaryText
                        )
                    }

                    IconButton(
                        onClick = {
                            if (participant != null) onNavigateToUserProfile(participant.id)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Chat details",
                            tint = OmigramPrimaryText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OmigramBackground
                )
            )
        },
        bottomBar = {
            Column {
                // Animated typing indicator
                AnimatedVisibility(
                    visible = chat?.isTyping == true,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    Box(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
                        TypingIndicator(userName = chat?.participant?.fullName ?: "Contact")
                    }
                }

                // Message composer
                MessageComposer(
                    text = uiState.inputText,
                    onTextChange = { viewModel.onInputTextChange(it) },
                    onSend = { viewModel.sendMessage() },
                    onAttachmentClick = { viewModel.toggleAttachmentSheet(true) },
                    onEmojiClick = { viewModel.toggleEmojiSheet(true) },
                    onMicClick = { viewModel.sendVoiceNoteSimulation() },
                    replyToMessage = uiState.replyingToMessage,
                    onCancelReply = { viewModel.setReplyingTo(null) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(OmigramBackground)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .testTag("chat_messages_list")
            ) {
                var lastDate = ""
                messages.forEachIndexed { index, message ->
                    val msgDate = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date(message.createdAt))
                    if (msgDate != lastDate) {
                        val isToday = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date()) ==
                                SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(message.createdAt))
                        val label = if (isToday) "Today" else msgDate
                        item(key = "date_${msgDate}_$index") {
                            DateSeparator(dateText = label)
                        }
                        lastDate = msgDate
                    }

                    item(key = message.id) {
                        val isCurrentUser = message.senderId == SampleData.CURRENT_USER_ID
                        MessageBubble(
                            message = message,
                            isCurrentUser = isCurrentUser,
                            onLongClick = { viewModel.selectMessageForMenu(message) },
                            onReactionClick = { emoji -> viewModel.toggleReaction(message.id, emoji) }
                        )
                    }
                }
            }
        }
    }

    // Message Long Press Action Dialog
    if (uiState.selectedMessageForMenu != null) {
        val selectedMsg = uiState.selectedMessageForMenu!!
        Dialog(onDismissRequest = { viewModel.selectMessageForMenu(null) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = OmigramBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, OmigramBorder, RoundedCornerShape(16.dp))
                    .testTag("message_actions_dialog")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Quick reaction emojis row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val reactionEmojis = listOf("❤️", "👍", "🔥", "😂", "😮", "🙏")
                        reactionEmojis.forEach { emoji ->
                            Surface(
                                shape = CircleShape,
                                color = OmigramSecondaryBackground,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        viewModel.toggleReaction(selectedMsg.id, emoji)
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = OmigramBorder
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setReplyingTo(selectedMsg)
                                viewModel.selectMessageForMenu(null)
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Reply, contentDescription = null, tint = OmigramAccentBlue)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Reply", fontSize = 14.sp, color = OmigramPrimaryText)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Message", selectedMsg.content)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                viewModel.selectMessageForMenu(null)
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = OmigramAccentBlue)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Copy text", fontSize = 14.sp, color = OmigramPrimaryText)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.deleteMessage(selectedMsg.id)
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = OmigramErrorRed)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Delete message", fontSize = 14.sp, color = OmigramErrorRed)
                    }
                }
            }
        }
    }

    // Attachment Modal Bottom Sheet
    if (uiState.isAttachmentSheetOpen) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleAttachmentSheet(false) },
            sheetState = sheetState,
            containerColor = OmigramBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Share Content",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmigramPrimaryText
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AttachmentOption(
                        icon = Icons.Default.CameraAlt,
                        label = "Camera",
                        color = Color(0xFFE11D48),
                        onClick = {
                            viewModel.sendAttachmentSimulation(
                                "IMAGE",
                                "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=500&auto=format&fit=crop&q=80"
                            )
                        }
                    )
                    AttachmentOption(
                        icon = Icons.Default.Image,
                        label = "Gallery",
                        color = OmigramAccentBlue,
                        onClick = {
                            viewModel.sendAttachmentSimulation(
                                "IMAGE",
                                "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=500&auto=format&fit=crop&q=80"
                            )
                        }
                    )
                    AttachmentOption(
                        icon = Icons.Default.Description,
                        label = "Document",
                        color = Color(0xFF7C3AED),
                        onClick = {
                            viewModel.sendAttachmentSimulation("DOC", "")
                        }
                    )
                    AttachmentOption(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        color = OnlineGreen,
                        onClick = {
                            viewModel.sendMessage()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Emoji Picker Sheet
    if (uiState.isEmojiSheetOpen) {
        val emojiSheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleEmojiSheet(false) },
            sheetState = emojiSheetState,
            containerColor = OmigramBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Reactions & Emojis",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmigramPrimaryText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val emojis = listOf(
                    "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣",
                    "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰",
                    "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜",
                    "👍", "👎", "👏", "🙌", "👐", "🤲", "🤝", "🙏",
                    "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍",
                    "🔥", "✨", "🎉", "🚀", "💯", "⭐", "⚡", "💡"
                )

                val rows = emojis.chunked(8)
                rows.forEach { rowEmojis ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        rowEmojis.forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.onInputTextChange(uiState.inputText + emoji)
                                        viewModel.toggleEmojiSheet(false)
                                    }
                                    .padding(4.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AttachmentOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = OmigramPrimaryText
        )
    }
}
