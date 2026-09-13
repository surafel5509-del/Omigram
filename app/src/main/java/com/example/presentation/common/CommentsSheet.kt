package com.example.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.core.model.Comment
import com.example.core.model.Post
import com.example.core.model.User
import com.example.data.local.SampleData
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramErrorRed
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSheet(
    post: Post,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var commentList by remember { mutableStateOf<List<Comment>>(SampleData.sampleComments) }
    var inputComment by remember { mutableStateOf("") }

    val quickEmojis = listOf("❤️", "🙌", "🔥", "👏", "😢", "😍", "✨", "🎉")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = OmigramBackground,
        modifier = modifier.testTag("comments_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .background(OmigramBackground)
        ) {
            // Drag handle and Title
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Comments",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmigramPrimaryText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                HorizontalDivider(color = OmigramBorder)
            }

            // Comments List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Post caption as original author entry
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            model = post.author.avatarUrl,
                            contentDescription = post.author.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(1.dp, OmigramBorder, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = OmigramPrimaryText)) {
                                        append(post.author.username)
                                    }
                                    append("  ")
                                    append(post.caption)
                                },
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = post.timeAgo,
                                fontSize = 11.sp,
                                color = OmigramSecondaryText
                            )
                        }
                    }
                    HorizontalDivider(color = OmigramBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))
                }

                items(commentList, key = { it.id }) { comment ->
                    var isLiked by remember { mutableStateOf(comment.isLiked) }
                    var likesCount by remember { mutableStateOf(comment.likesCount) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            model = comment.user.avatarUrl,
                            contentDescription = comment.user.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .border(0.5.dp, OmigramBorder, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = OmigramPrimaryText)) {
                                        append(comment.user.username)
                                    }
                                    append("  ")
                                    append(comment.text)
                                },
                                fontSize = 13.sp,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = comment.createdAt,
                                    fontSize = 11.sp,
                                    color = OmigramSecondaryText
                                )
                                if (likesCount > 0) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "$likesCount likes",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OmigramSecondaryText
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Reply",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OmigramSecondaryText,
                                    modifier = Modifier.clickable {
                                        inputComment = "@${comment.user.username} "
                                    }
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                isLiked = !isLiked
                                likesCount += if (isLiked) 1 else -1
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like comment",
                                tint = if (isLiked) OmigramErrorRed else OmigramSecondaryText,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Quick emoji reactions bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                quickEmojis.forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .clickable { inputComment += emoji }
                            .padding(4.dp)
                    )
                }
            }

            HorizontalDivider(color = OmigramBorder)

            // Post comment input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = SampleData.currentUser.avatarUrl,
                    contentDescription = "My Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .border(1.dp, OmigramBorder, CircleShape)
                )

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedTextField(
                    value = inputComment,
                    onValueChange = { inputComment = it },
                    placeholder = {
                        Text(
                            text = "Add a comment...",
                            fontSize = 13.sp,
                            color = OmigramSecondaryText
                        )
                    },
                    shape = RoundedCornerShape(999.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = OmigramSecondaryBackground,
                        unfocusedContainerColor = OmigramSecondaryBackground,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = OmigramPrimaryText,
                        unfocusedTextColor = OmigramPrimaryText
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("comment_input_field")
                )

                if (inputComment.isNotBlank()) {
                    TextButton(
                        onClick = {
                            val newComment = Comment(
                                id = "c_${System.currentTimeMillis()}",
                                user = SampleData.currentUser.let {
                                    User(
                                        id = it.id,
                                        username = it.username,
                                        fullName = it.fullName,
                                        avatarUrl = it.avatarUrl,
                                        bio = it.bio,
                                        phone = it.phone,
                                        email = it.email,
                                        isOnline = it.isOnline,
                                        lastSeen = it.lastSeen,
                                        createdAt = it.createdAt,
                                        updatedAt = it.updatedAt
                                    )
                                },
                                text = inputComment.trim(),
                                createdAt = "Just now",
                                likesCount = 0,
                                isLiked = false
                            )
                            commentList = commentList + newComment
                            inputComment = ""
                        },
                        modifier = Modifier.testTag("post_comment_button")
                    ) {
                        Text(
                            text = "Post",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OmigramAccentBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
