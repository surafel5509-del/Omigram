package com.example.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.Post
import com.example.core.model.User
import com.example.data.local.SampleData
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText

data class PhotoFilter(val name: String, val tint: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    onDismiss: () -> Unit,
    onPostCreated: (Post) -> Unit,
    modifier: Modifier = Modifier
) {
    val sampleGalleryImages = listOf(
        "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=800&auto=format&fit=crop&q=80"
    )

    val filters = listOf(
        PhotoFilter("Normal", Color.Transparent),
        PhotoFilter("Clarendon", Color(0x220095F6)),
        PhotoFilter("Juno", Color(0x22FF8A00)),
        PhotoFilter("Valencia", Color(0x22F472B6)),
        PhotoFilter("Moon", Color(0x44000000)),
        PhotoFilter("Ludwig", Color(0x2210B981))
    )

    var selectedImage by remember { mutableStateOf(sampleGalleryImages.first()) }
    var selectedFilter by remember { mutableStateOf(filters.first()) }
    var caption by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("San Francisco, California") }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(OmigramBackground)
            .testTag("create_post_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Post",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OmigramPrimaryText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = OmigramPrimaryText
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val newPost = Post(
                                id = "post_${System.currentTimeMillis()}",
                                author = SampleData.currentUser.let {
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
                                imageUrl = selectedImage,
                                caption = caption.ifBlank { "Just sharing a new moment on Omigram! #minimal #aesthetic" },
                                location = location,
                                likesCount = 1,
                                commentsCount = 0,
                                timeAgo = "Just now",
                                isLiked = false,
                                isBookmarked = false
                            )
                            onPostCreated(newPost)
                        },
                        modifier = Modifier.testTag("create_post_share_button")
                    ) {
                        Text(
                            text = "Share",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OmigramAccentBlue
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
                .verticalScroll(rememberScrollState())
                .background(OmigramBackground)
        ) {
            // Selected Image Preview with Filter overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(OmigramSecondaryBackground)
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = "Selected photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Filter tint
                if (selectedFilter.tint != Color.Transparent) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(selectedFilter.tint)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filters selector row
            Text(
                text = "Filters",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = OmigramSecondaryText,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedFilter = filter }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) OmigramAccentBlue else OmigramBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            AsyncImage(
                                model = selectedImage,
                                contentDescription = filter.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            if (filter.tint != Color.Transparent) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(filter.tint)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = filter.name,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) OmigramAccentBlue else OmigramPrimaryText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = OmigramBorder)

            // Caption input row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = SampleData.currentUser.avatarUrl,
                    contentDescription = "My avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.dp, OmigramBorder, CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    placeholder = {
                        Text(
                            text = "Write a caption...",
                            fontSize = 14.sp,
                            color = OmigramSecondaryText
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = OmigramPrimaryText,
                        unfocusedTextColor = OmigramPrimaryText
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("create_caption_input")
                )
            }

            HorizontalDivider(color = OmigramBorder)

            // Add Location row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* edit location */ }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = OmigramPrimaryText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Add Location",
                    fontSize = 14.sp,
                    color = OmigramPrimaryText,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = location,
                    fontSize = 13.sp,
                    color = OmigramSecondaryText
                )
            }

            HorizontalDivider(color = OmigramBorder)

            // Tag People row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* tag people */ }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Tag People",
                    tint = OmigramPrimaryText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Tag People",
                    fontSize = 14.sp,
                    color = OmigramPrimaryText
                )
            }

            HorizontalDivider(color = OmigramBorder)

            // Choose other image from gallery
            Text(
                text = "Recent Photos",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = OmigramSecondaryText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sampleGalleryImages.forEach { imgUrl ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { selectedImage = imgUrl }
                            .border(
                                width = if (selectedImage == imgUrl) 2.dp else 0.dp,
                                color = OmigramAccentBlue,
                                shape = RoundedCornerShape(6.dp)
                            )
                    ) {
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = "Gallery item",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
