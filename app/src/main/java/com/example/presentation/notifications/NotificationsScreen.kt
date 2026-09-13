package com.example.presentation.notifications

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.SocialNotification
import com.example.core.model.SocialNotificationType
import com.example.data.local.SampleData
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.appBackground
import com.example.ui.theme.appBorder
import com.example.ui.theme.appSurface
import com.example.ui.theme.appTextPrimary
import com.example.ui.theme.appTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToUserProfile: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var notifications by remember { mutableStateOf(SampleData.sampleNotifications) }
    var followingMap by remember { mutableStateOf(mapOf("user_2" to true, "user_4" to false, "user_5" to true)) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(appBackground)
            .testTag("notifications_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = appTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("notifications_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = appTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBackground,
                    titleContentColor = appTextPrimary,
                    navigationIconContentColor = appTextPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(appBackground)
                .testTag("notifications_list")
        ) {
            val todayList = notifications.take(3)
            val thisWeekList = notifications.drop(3).take(3)
            val earlierList = notifications.drop(6)

            if (todayList.isNotEmpty()) {
                item {
                    NotificationSectionTitle(title = "Today")
                }
                items(todayList, key = { it.id }) { item ->
                    NotificationRow(
                        item = item,
                        isFollowing = followingMap[item.actor.id] ?: item.isFollowingBack,
                        onFollowToggle = {
                            val cur = followingMap[item.actor.id] ?: item.isFollowingBack
                            followingMap = followingMap + (item.actor.id to !cur)
                        },
                        onUserClick = { onNavigateToUserProfile(item.actor.id) }
                    )
                }
            }

            if (thisWeekList.isNotEmpty()) {
                item {
                    NotificationSectionTitle(title = "This Week")
                }
                items(thisWeekList, key = { it.id }) { item ->
                    NotificationRow(
                        item = item,
                        isFollowing = followingMap[item.actor.id] ?: item.isFollowingBack,
                        onFollowToggle = {
                            val cur = followingMap[item.actor.id] ?: item.isFollowingBack
                            followingMap = followingMap + (item.actor.id to !cur)
                        },
                        onUserClick = { onNavigateToUserProfile(item.actor.id) }
                    )
                }
            }

            if (earlierList.isNotEmpty()) {
                item {
                    NotificationSectionTitle(title = "Earlier")
                }
                items(earlierList, key = { it.id }) { item ->
                    NotificationRow(
                        item = item,
                        isFollowing = followingMap[item.actor.id] ?: item.isFollowingBack,
                        onFollowToggle = {
                            val cur = followingMap[item.actor.id] ?: item.isFollowingBack
                            followingMap = followingMap + (item.actor.id to !cur)
                        },
                        onUserClick = { onNavigateToUserProfile(item.actor.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = appTextPrimary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
private fun NotificationRow(
    item: SocialNotification,
    isFollowing: Boolean,
    onFollowToggle: () -> Unit,
    onUserClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Avatar
        AsyncImage(
            model = item.actor.avatarUrl,
            contentDescription = item.actor.fullName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(1.dp, appBorder, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Text notification description
        val annotatedText = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = appTextPrimary)) {
                append(item.actor.username)
            }
            append(" ")
            append(
                when (item.type) {
                    SocialNotificationType.LIKE -> "liked your photo."
                    SocialNotificationType.COMMENT -> "commented: \"${item.commentText ?: "Awesome!"}\""
                    SocialNotificationType.FOLLOW -> "started following you."
                }
            )
            append(" ")
            withStyle(SpanStyle(color = appTextSecondary, fontSize = 12.sp)) {
                append(item.timeAgo)
            }
        }

        Text(
            text = annotatedText,
            fontSize = 13.sp,
            lineHeight = 17.sp,
            color = appTextPrimary,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Action or media thumbnail on the right
        if (item.type == SocialNotificationType.FOLLOW) {
            if (isFollowing) {
                OutlinedButton(
                    onClick = onFollowToggle,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = appTextPrimary),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("notification_following_${item.actor.id}")
                ) {
                    Text("Following", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            } else {
                Button(
                    onClick = onFollowToggle,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OmigramAccentBlue),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("notification_follow_${item.actor.id}")
                ) {
                    Text("Follow", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        } else if (item.postPreviewUrl != null) {
            AsyncImage(
                model = item.postPreviewUrl,
                contentDescription = "Post thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(0.5.dp, appBorder, RoundedCornerShape(6.dp))
            )
        }
    }
}
