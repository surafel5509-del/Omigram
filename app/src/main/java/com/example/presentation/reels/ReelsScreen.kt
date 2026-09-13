package com.example.presentation.reels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.Reel
import com.example.data.local.SampleData
import com.example.presentation.common.CommentProIcon
import com.example.presentation.common.DmProIcon
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramErrorRed

import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateListOf
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.presentation.create.CreateScreen
import com.example.ui.theme.OmigramOrange

import com.example.presentation.common.ShareWithFriendsDialog

@Composable
fun ReelsScreen(
    onNavigateToUserProfile: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val reels = remember { mutableStateListOf(*SampleData.sampleReels.toTypedArray()) }
    var isCreateOpen by remember { mutableStateOf(false) }
    var sharingReel by remember { mutableStateOf<Reel?>(null) }
    val pagerState = rememberPagerState(pageCount = { reels.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("reels_screen")
    ) {
        if (reels.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No reels yet", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { isCreateOpen = true },
                        colors = ButtonDefaults.buttonColors(containerColor = OmigramOrange)
                    ) {
                        Text("Create Reel", color = Color.White)
                    }
                }
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val reel = reels.getOrNull(page)
                if (reel != null) {
                    ReelPlayerItem(
                        reel = reel,
                        onUserClick = { onNavigateToUserProfile(reel.creator.id) },
                        onShareClick = { sharingReel = reel },
                        onDeleteReel = {
                            reels.remove(reel)
                            Toast.makeText(context, "Reel deleted", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        // Top Bar: "Reels" + Camera / Upload Reel button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 36.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reels",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(
                onClick = { isCreateOpen = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Upload Reel",
                    tint = OmigramOrange,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    if (sharingReel != null) {
        val r = sharingReel!!
        ShareWithFriendsDialog(
            title = "Share Reel",
            contentPreview = "🎥 Reel by @${r.creator.username}: ${r.caption}",
            onDismiss = { sharingReel = null }
        )
    }

    if (isCreateOpen) {
        CreateScreen(
            onDismiss = { isCreateOpen = false },
            onPostCreated = { isCreateOpen = false },
            onStoryCreated = { isCreateOpen = false },
            onReelCreated = { newReel ->
                reels.add(0, newReel)
                isCreateOpen = false
                Toast.makeText(context, "Reel created successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun ReelPlayerItem(
    reel: Reel,
    onUserClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteReel: () -> Unit = {}
) {
    val isCurrentUser = reel.creator.id == SampleData.CURRENT_USER_ID
    var isLiked by remember { mutableStateOf(reel.isLiked) }
    var likesCount by remember { mutableStateOf(reel.likesCount) }
    var isFollowing by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { isPaused = !isPaused },
                    onDoubleTap = {
                        if (!isLiked) {
                            isLiked = true
                            likesCount += 1
                        }
                    }
                )
            }
            .testTag("reel_item_${reel.id}")
    ) {
        // Fullscreen poster / simulated video frame
        AsyncImage(
            model = reel.videoThumbnailUrl,
            contentDescription = reel.caption,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Top information banner: Creator Profile ONLY (No Sound/Audio Track)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 80.dp, start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.Black.copy(alpha = 0.55f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = reel.creator.avatarUrl,
                    contentDescription = reel.creator.fullName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, OmigramOrange, CircleShape)
                        .clickable { onUserClick() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "@${reel.creator.username}",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.clickable { onUserClick() }
                )
                Spacer(modifier = Modifier.width(10.dp))
                if (isFollowing) {
                    OutlinedButton(
                        onClick = { isFollowing = false },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text("Following", fontSize = 11.sp, color = Color.White)
                    }
                } else {
                    Button(
                        onClick = { isFollowing = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OmigramOrange),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text("Follow", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Paused icon overlay
        AnimatedVisibility(
            visible = isPaused,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.55f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Paused",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        // Right side vertical action rail
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        isLiked = !isLiked
                        likesCount += if (isLiked) 1 else -1
                    }
                    .testTag("reel_like_${reel.id}")
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isLiked) OmigramErrorRed else Color.White,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = likesCount.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Comment
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { /* open comments */ }
            ) {
                CommentProIcon(
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reel.commentsCount.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Share
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onShareClick() }
            ) {
                DmProIcon(
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Share",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Overflow 3-dots
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    if (isCurrentUser) {
                        DropdownMenuItem(
                            text = { Text("Delete Reel", color = OmigramErrorRed, fontWeight = FontWeight.Bold) },
                            leadingIcon = {
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = OmigramErrorRed)
                            },
                            onClick = {
                                showMenu = false
                                onDeleteReel()
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Bookmark") },
                        leadingIcon = {
                            Icon(Icons.Default.BookmarkBorder, contentDescription = null)
                        },
                        onClick = { showMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Share to Chat") },
                        leadingIcon = {
                            Icon(Icons.Default.Share, contentDescription = null)
                        },
                        onClick = {
                            showMenu = false
                            onShareClick()
                        }
                    )
                }
            }

            // Audio track disk
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = reel.creator.avatarUrl,
                    contentDescription = "Audio disc",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Bottom left caption and audio line (Creator profile removed from bottom)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 76.dp, bottom = 28.dp)
        ) {
            // Caption
            Text(
                text = reel.caption,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Audio track name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Audio track",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = reel.audioTrackTitle,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1
                )
            }
        }
    }
}
