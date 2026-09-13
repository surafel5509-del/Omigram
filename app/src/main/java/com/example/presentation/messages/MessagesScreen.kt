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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.Chat
import com.example.core.model.MessageStatus
import com.example.data.local.SampleData
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryText
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialPillDark
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
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    val filterTabs = listOf("All", "Unread", "Direct", "Pinned")

    val chats = remember { SampleData.sampleChats.distinctBy { it.id } }
    val activeUsers = remember(chats) {
        SampleData.sampleUsers
            .filter { it.isOnline && it.id != SampleData.CURRENT_USER_ID }
            .distinctBy { it.id }
    }

    val filteredChats = remember(searchQuery, selectedFilterIndex, chats) {
        val searched = if (searchQuery.isBlank()) {
            chats
        } else {
            chats.filter {
                it.participant.fullName.contains(searchQuery, ignoreCase = true) ||
                it.participant.username.contains(searchQuery, ignoreCase = true) ||
                it.lastMessage?.content?.contains(searchQuery, ignoreCase = true) == true
            }
        }

        when (selectedFilterIndex) {
            1 -> searched.filter { it.unreadCount > 0 }
            3 -> searched.filter { it.isPinned }
            else -> searched
        }.distinctBy { it.id }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(OmigramBackground)
            .testTag("messages_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Messages",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                        Text(
                            text = "${activeUsers.size} friends online",
                            fontSize = 12.sp,
                            color = OnlineGreen
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
                            tint = Color(0xFF1A1A1A)
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF4F5F7),
                        modifier = Modifier.size(38.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (chats.isNotEmpty()) {
                                    onNavigateToChat(chats.first().id)
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("messages_compose_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "New Conversation",
                                tint = Color(0xFF1A1A1A),
                                modifier = Modifier.size(18.dp)
                            )
                        }
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
            // ==========================================
            // Modern Search Bar (Enhanced & Responsive)
            // ==========================================
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBECEF)),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Search chats, contacts & messages...",
                                fontSize = 14.sp,
                                color = Color(0xFF8E8E93)
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color(0xFF1A1A1A),
                            unfocusedTextColor = Color(0xFF1A1A1A)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("messages_search_bar")
                    )

                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = Color(0xFF8E8E93),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Filter Chips Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterTabs.indices.toList()) { index ->
                    val isSelected = selectedFilterIndex == index
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SocialPillDark else Color(0xFFF4F5F7),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) SocialPillDark else Color(0xFFE5E5EA)
                        ),
                        modifier = Modifier.clickable { selectedFilterIndex = index }
                    ) {
                        Text(
                            text = filterTabs[index],
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF1A1A1A),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // ==========================================
            // Active Friends Horizontal Row (Luxury Ring)
            // ==========================================
            if (activeUsers.isNotEmpty() && searchQuery.isBlank()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
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
                                modifier = Modifier.size(62.dp),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White,
                                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE5E7EB)),
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.fillMaxSize()
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

                                // Online indicator
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(OnlineGreen)
                                        .border(2.dp, Color.White, CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = user.fullName.split(" ").firstOrNull() ?: user.username,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1A1A1A),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // ==========================================
            // Conversation List
            // ==========================================
            if (filteredChats.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No conversations found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try searching for another keyword or contact name",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                        .testTag("messages_chat_list")
                ) {
                    items(filteredChats, key = { it.id }) { chat ->
                        ConversationCardItem(
                            chat = chat,
                            onClick = { onNavigateToChat(chat.id) }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationCardItem(
    chat: Chat,
    onClick: () -> Unit
) {
    val lastMsg = chat.lastMessage
    val formattedTime = remember(lastMsg?.createdAt) {
        if (lastMsg == null) ""
        else {
            val now = System.currentTimeMillis()
            val diff = now - lastMsg.createdAt
            when {
                diff < 60_000L -> "now"
                diff < 3600_000L -> "${diff / 60_000L}m"
                diff < 86400_000L -> SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(lastMsg.createdAt))
                else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(lastMsg.createdAt))
            }
        }
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F1F3)),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("chat_item_${chat.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with online status
            Box(
                modifier = Modifier.size(52.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    model = chat.participant.avatarUrl,
                    contentDescription = chat.participant.fullName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )

                if (chat.participant.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .background(OnlineGreen)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Name + last message preview
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.participant.fullName,
                        fontSize = 15.sp,
                        fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = formattedTime,
                        fontSize = 11.5.sp,
                        color = if (chat.unreadCount > 0) SocialBrandBlue else Color(0xFF8E8E93),
                        fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (lastMsg != null && lastMsg.senderId == SampleData.CURRENT_USER_ID) {
                            Icon(
                                imageVector = if (lastMsg.status == MessageStatus.READ) Icons.Default.DoneAll else Icons.Default.Done,
                                contentDescription = null,
                                tint = if (lastMsg.status == MessageStatus.READ) SocialBrandBlue else Color(0xFF8E8E93),
                                modifier = Modifier
                                    .size(14.dp)
                                    .padding(end = 4.dp)
                            )
                        }

                        Text(
                            text = lastMsg?.content ?: "No messages yet",
                            fontSize = 13.sp,
                            color = if (chat.unreadCount > 0) Color(0xFF1A1A1A) else Color(0xFF8E8E93),
                            fontWeight = if (chat.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (chat.unreadCount > 0) {
                        Surface(
                            shape = CircleShape,
                            color = SocialBrandBlue,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${chat.unreadCount}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
