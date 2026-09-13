package com.example.presentation.messages

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.Chat
import com.example.data.local.SampleData
import com.example.ui.theme.InstagramGradient
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText
import com.example.ui.theme.OnlineGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val chats = SampleData.sampleChats
    val activeUsers = SampleData.sampleUsers.filter { it.isOnline }

    val filteredChats = if (searchQuery.isBlank()) {
        chats
    } else {
        chats.filter {
            it.participant.fullName.contains(searchQuery, ignoreCase = true) ||
            it.participant.username.contains(searchQuery, ignoreCase = true) ||
            it.lastMessage?.content?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(OmigramBackground)
            .testTag("messages_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "alexmercer",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OmigramPrimaryText
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("messages_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OmigramPrimaryText
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (chats.isNotEmpty()) {
                                onNavigateToChat(chats.first().id)
                            }
                        },
                        modifier = Modifier.testTag("messages_compose_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Compose",
                            tint = OmigramPrimaryText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OmigramBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(OmigramBackground)
        ) {
            // Search bar: Rounded pill, gray background (#FAFAFA)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search",
                        fontSize = 14.sp,
                        color = OmigramSecondaryText
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = OmigramSecondaryText,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(999.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = OmigramSecondaryBackground,
                    unfocusedContainerColor = OmigramSecondaryBackground,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = OmigramPrimaryText,
                    unfocusedTextColor = OmigramPrimaryText
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .height(44.dp)
                    .testTag("messages_search_bar")
            )

            // Story-style active users row at top (circular avatars with gradient ring)
            if (activeUsers.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                ) {
                    items(activeUsers, key = { it.id }) { user ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    val chat = chats.find { it.participant.id == user.id }
                                    if (chat != null) onNavigateToChat(chat.id)
                                    else if (chats.isNotEmpty()) onNavigateToChat(chats.first().id)
                                }
                                .testTag("active_user_${user.id}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(InstagramGradient))
                                    .padding(2.5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(OmigramBackground)
                                        .padding(2.dp)
                                ) {
                                    AsyncImage(
                                        model = user.avatarUrl,
                                        contentDescription = user.fullName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = user.fullName.split(" ").firstOrNull() ?: user.username,
                                fontSize = 11.sp,
                                color = OmigramPrimaryText,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Section Header: Messages
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Messages",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmigramPrimaryText
                )
                Text(
                    text = "Requests (2)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OmigramAccentBlue
                )
            }

            // Chat items list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("messages_chat_list")
            ) {
                items(filteredChats, key = { it.id }) { chat ->
                    DirectMessageItem(
                        chat = chat,
                        onClick = { onNavigateToChat(chat.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DirectMessageItem(
    chat: Chat,
    onClick: () -> Unit
) {
    val participant = chat.participant
    val lastMsg = chat.lastMessage
    val timeFormatted = lastMsg?.let {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it.createdAt))
    } ?: "now"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular avatar (56px) with online green dot
        Box(modifier = Modifier.size(56.dp)) {
            AsyncImage(
                model = participant.avatarUrl,
                contentDescription = participant.fullName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(0.5.dp, OmigramBorder, CircleShape)
            )

            if (participant.isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(OnlineGreen)
                        .border(2.dp, OmigramBackground, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Username and last message preview
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = participant.fullName,
                fontSize = 15.sp,
                fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                color = OmigramPrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = lastMsg?.content ?: "Sent a message",
                    fontSize = 13.sp,
                    color = if (chat.unreadCount > 0) OmigramPrimaryText else OmigramSecondaryText,
                    fontWeight = if (chat.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = " • $timeFormatted",
                    fontSize = 12.sp,
                    color = OmigramSecondaryText
                )
            }
        }

        // Right side: Unread blue badge or camera icon
        if (chat.unreadCount > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(OmigramAccentBlue)
            )
        }
    }
}
