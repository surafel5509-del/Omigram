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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.example.data.remote.supabase.SupabaseClientProvider
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialPeachGradientTop
import com.example.ui.theme.SocialPeachGradientMid
import com.example.ui.theme.SocialPeachGradientBottom
import com.example.ui.theme.SocialPillDark

data class PhotoFilter(val name: String, val tint: Color)

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
        PhotoFilter("Original", Color.Transparent),
        PhotoFilter("Warmth", Color(0x22FF8A00)),
        PhotoFilter("Pastel", Color(0x22F472B6)),
        PhotoFilter("Azure", Color(0x220095F6)),
        PhotoFilter("Noir", Color(0x35000000)),
        PhotoFilter("Emerald", Color(0x2210B981))
    )

    val quickLocations = listOf("San Francisco, CA", "Addis Ababa, ET", "New York, NY", "Tokyo, JP", "Paris, FR")
    val quickTags = listOf("#minimal", "#aesthetic", "#lifestyle", "#travel", "#design")

    var selectedImage by remember { mutableStateOf(sampleGalleryImages.first()) }
    var selectedFilter by remember { mutableStateOf(filters.first()) }
    var caption by remember { mutableStateOf("") }
    var location by remember { mutableStateOf(quickLocations.first()) }
    var isPrivate by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("create_post_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SocialPeachGradientTop,
                            SocialPeachGradientMid,
                            SocialPeachGradientBottom
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Modern Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Close Button
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(4.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.06f)),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("create_post_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = Color(0xFF1A1A1A),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = "New Creation",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                // Share Button (Sleek Dark Pill)
                Button(
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
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SocialPillDark
                    ),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("create_post_share_button")
                ) {
                    Text(
                        text = "Publish",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Photo Card Container
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(26.dp),
                        spotColor = Color(0xFFD4A373).copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(26.dp),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.05f)
                ) {
                    AsyncImage(
                        model = selectedImage,
                        contentDescription = "Selected photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Filter tint overlay
                    if (selectedFilter.tint != Color.Transparent) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(selectedFilter.tint)
                        )
                    }

                    // Filter name chip indicator on top-right
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = selectedFilter.name,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    // Supabase ready sync pill indicator on bottom-left
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF34C759))
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = if (SupabaseClientProvider.isConfigured) "Supabase Sync" else "Cloud Ready",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gallery Thumbnails Row
            Text(
                text = "Choose Photo",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(sampleGalleryImages) { imgUrl ->
                    val isSelected = selectedImage == imgUrl
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedImage = imgUrl }
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) SocialBrandBlue else Color(0xFFEBECEF),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = "Gallery option",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filters Carousel
            Text(
                text = "Style Filters",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) SocialPillDark else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) SocialPillDark else Color(0xFFE5E5EA)
                        ),
                        modifier = Modifier.clickable { selectedFilter = filter }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (filter.tint != Color.Transparent) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(filter.tint.copy(alpha = 1f))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = filter.name,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF1A1A1A)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Caption Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.04f)),
                shape = RoundedCornerShape(22.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            model = SampleData.currentUser.avatarUrl,
                            contentDescription = "My avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Color(0xFFE5E5EA), CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedTextField(
                            value = caption,
                            onValueChange = { caption = it },
                            placeholder = {
                                Text(
                                    text = "Write a thoughtful caption...",
                                    fontSize = 14.sp,
                                    color = Color(0xFF8E8E93)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = Color(0xFF1A1A1A),
                                unfocusedTextColor = Color(0xFF1A1A1A)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .testTag("create_caption_input")
                        )
                    }

                    // Quick hashtag chips
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(quickTags) { tag ->
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFF4F5F7),
                                modifier = Modifier.clickable {
                                    caption = if (caption.isEmpty()) tag else "$caption $tag"
                                }
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SocialBrandBlue,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Location Selector Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.04f)),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color(0xFF1A1A1A),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = location,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1A1A1A)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(quickLocations) { loc ->
                            val isLocSelected = location == loc
                            Surface(
                                shape = CircleShape,
                                color = if (isLocSelected) Color(0xFF1A1A1A) else Color(0xFFF4F5F7),
                                modifier = Modifier.clickable { location = loc }
                            ) {
                                Text(
                                    text = loc,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isLocSelected) Color.White else Color(0xFF555555),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Privacy mode toggle (Public vs Private)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.04f))
                    .clickable { isPrivate = !isPrivate },
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isPrivate) Icons.Default.Lock else Icons.Default.Public,
                            contentDescription = "Visibility",
                            tint = if (isPrivate) Color(0xFF34C759) else Color(0xFF1A1A1A),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isPrivate) "Private Mode" else "Public to Everyone",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = if (isPrivate) "Only you and approved contacts can view" else "Visible to all followers and community",
                                fontSize = 12.sp,
                                color = Color(0xFF8E8E93)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (isPrivate) Color(0xFF34C759).copy(alpha = 0.15f) else Color(0xFFF4F5F7)
                    ) {
                        Text(
                            text = if (isPrivate) "Secured" else "Public",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPrivate) Color(0xFF34C759) else Color(0xFF1A1A1A),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
