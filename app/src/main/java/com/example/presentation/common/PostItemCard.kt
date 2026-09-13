package com.example.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.Post
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramErrorRed
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.MoreHoriz

@Composable
fun PostItemCard(
    post: Post,
    onLikeToggle: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveToggle: () -> Unit,
    onUserClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var showDoubleTapHeart by remember { mutableStateOf(false) }
    val heartScale = remember { Animatable(0f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(OmigramBackground)
            .padding(vertical = 8.dp)
            .testTag("post_item_${post.id}")
    ) {
        // Post Header: Avatar (40dp), full name, @username, time, 3-dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = post.author.avatarUrl,
                contentDescription = post.author.fullName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .clickable { onUserClick() }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onUserClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = post.author.fullName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = OmigramPrimaryText
                    )
                    Text(
                        text = "@${post.author.username}",
                        fontSize = 13.sp,
                        color = OmigramSecondaryText
                    )
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = OmigramSecondaryText
                    )
                    Text(
                        text = post.timeAgo,
                        fontSize = 13.sp,
                        color = OmigramSecondaryText
                    )
                }
                if (post.location.isNotEmpty()) {
                    Text(
                        text = post.location,
                        fontSize = 11.sp,
                        color = OmigramSecondaryText
                    )
                }
            }

            IconButton(
                onClick = { },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "Post options",
                    tint = OmigramSecondaryText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Post Text / Caption placed ABOVE media (as shown in Screenshot 1)
        if (post.caption.isNotEmpty()) {
            Text(
                text = post.caption,
                fontSize = 14.5.sp,
                lineHeight = 21.sp,
                color = OmigramPrimaryText,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }

        // Post Media with rounded corners (20dp) matching Screenshot 1
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(20.dp))
                .aspectRatio(1.15f)
                .background(OmigramSecondaryBackground)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (!post.isLiked) {
                                onLikeToggle()
                            }
                            coroutineScope.launch {
                                showDoubleTapHeart = true
                                heartScale.snapTo(0.2f)
                                heartScale.animateTo(1.2f, animationSpec = tween(180))
                                heartScale.animateTo(1.0f, animationSpec = tween(120))
                                delay(400)
                                showDoubleTapHeart = false
                            }
                        }
                    )
                }
        ) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = post.caption,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Animated Double-Tap Heart
            if (showDoubleTapHeart) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                        .scale(heartScale.value)
                )
            }
        }

        // Action Bar: Like, Comment, Share, Bookmark
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Heart Like
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onLikeToggle() }
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (post.isLiked) "Unlike" else "Like",
                        tint = if (post.isLiked) OmigramErrorRed else OmigramPrimaryText,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatCount(post.likesCount),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = OmigramPrimaryText
                    )
                }

                // Comment
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onCommentClick() }
                ) {
                    CommentProIcon(
                        tint = OmigramPrimaryText,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = post.commentsCount.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = OmigramPrimaryText
                    )
                }

                // Share / Send
                DmProIcon(
                    tint = OmigramPrimaryText,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onShareClick() }
                )
            }

            // Save / Bookmark
            Icon(
                imageVector = if (post.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = if (post.isBookmarked) "Unsave" else "Save",
                tint = OmigramPrimaryText,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSaveToggle() }
            )
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%,d", count)
        else -> count.toString()
    }
}
