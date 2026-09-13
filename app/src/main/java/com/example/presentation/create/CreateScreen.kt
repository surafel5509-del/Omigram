package com.example.presentation.create

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.Post
import com.example.core.model.Reel
import com.example.core.model.Story
import com.example.core.model.User
import com.example.data.local.SampleData
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialPeachGradientBottom
import com.example.ui.theme.SocialPeachGradientMid
import com.example.ui.theme.SocialPeachGradientTop
import com.example.ui.theme.SocialPillDark

data class PhotoFilter(val name: String, val tint: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    onDismiss: () -> Unit,
    onPostCreated: (Post) -> Unit,
    onStoryCreated: (Story) -> Unit = {},
    onReelCreated: (Reel) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val sampleGalleryImages = listOf(
        "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=800&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&auto=format&fit=crop&q=80"
    )

    val filters = listOf(
        PhotoFilter("Original", Color.Transparent),
        PhotoFilter("Warmth", Color(0x22FF8A00)),
        PhotoFilter("Pastel", Color(0x22F472B6)),
        PhotoFilter("Azure", Color(0x220095F6)),
        PhotoFilter("Noir", Color(0x35000000)),
        PhotoFilter("Golden", Color(0x25F59E0B)),
        PhotoFilter("Emerald", Color(0x2210B981))
    )

    val quickLocations = listOf("San Francisco, CA", "Addis Ababa, ET", "New York, NY", "Tokyo, JP", "Paris, FR")
    val quickTags = listOf("#minimal", "#aesthetic", "#lifestyle", "#travel", "#design")

    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPresetImage by remember { mutableStateOf(sampleGalleryImages.first()) }
    var isVideoMedia by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf(filters.first()) }
    var caption by remember { mutableStateOf("") }
    var location by remember { mutableStateOf(quickLocations.first()) }
    var isPrivate by remember { mutableStateOf(false) }

    var showDestinationPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Android Official Zero-Permission Photo and Video Picker
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedMediaUri = uri
            val type = context.contentResolver.getType(uri)
            isVideoMedia = type?.startsWith("video/") == true
            Toast.makeText(
                context,
                if (isVideoMedia) "Video selected from device" else "Image selected from device",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val currentDisplayMedia: Any = selectedMediaUri ?: selectedPresetImage

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

                // Quick draft indicator
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.9f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E5EA))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SocialBrandBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Live", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Media Preview Card
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
                        .aspectRatio(1.08f)
                ) {
                    AsyncImage(
                        model = currentDisplayMedia,
                        contentDescription = "Selected media",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Filter tint overlay
                    if (selectedFilter.tint != Color.Transparent && !isVideoMedia) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(selectedFilter.tint)
                        )
                    }

                    // Top Right: Filter / Media badge
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.55f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = if (isVideoMedia) "Video Media" else selectedFilter.name,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    // Button to trigger device media picker directly on the preview
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.94f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(14.dp)
                            .clickable {
                                mediaPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                )
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = "Pick Media",
                                tint = SocialBrandBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Choose Device Media",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Gallery Selector Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        mediaPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                        )
                    }
                    .testTag("pick_from_phone_button"),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBECEF))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(38.dp),
                            shape = CircleShape,
                            color = SocialBrandBlue.copy(alpha = 0.12f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "From Phone",
                                    tint = SocialBrandBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Select from Phone Storage",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = "Browse all images & videos on your device",
                                fontSize = 11.5.sp,
                                color = Color(0xFF8E8E93)
                            )
                        }
                    }

                    Text(
                        text = "Browse",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = SocialBrandBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Preset sample thumbnails
            Text(
                text = "Preset Inspirations",
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
                    val isSelected = selectedMediaUri == null && selectedPresetImage == imgUrl
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                selectedMediaUri = null
                                selectedPresetImage = imgUrl
                                isVideoMedia = false
                            }
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
                text = "Filters & Tone",
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

            Spacer(modifier = Modifier.height(16.dp))

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
                                    text = "Write your thoughts and story...",
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

                    // Hashtag chips
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

            // Privacy mode toggle
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
                                text = if (isPrivate) "Only approved connections see this" else "Visible to all community members",
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

            Spacer(modifier = Modifier.height(24.dp))

            // DRAFT and POST Action Buttons (as requested)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Draft Button
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Saved to Drafts!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF1A1A1A)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF1A1A1A)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("save_draft_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Save Draft",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Save Draft",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Post Button -> Opens Destination Picker (History vs Real Post vs Reel)
                Button(
                    onClick = {
                        showDestinationPicker = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SocialPillDark
                    ),
                    modifier = Modifier
                        .weight(1.2f)
                        .height(52.dp)
                        .testTag("create_post_publish_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Post",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Post",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }

    // Modal to choose Destination: History Post (Story) vs Home/Real Post vs Reel Post
    if (showDestinationPicker) {
        ModalBottomSheet(
            onDismissRequest = { showDestinationPicker = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Where to share?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "Select destination for your new creation",
                    fontSize = 13.sp,
                    color = Color(0xFF8E8E93),
                    modifier = Modifier.padding(top = 2.dp, bottom = 18.dp)
                )

                // Option 1: Real / Home Post
                DestinationOptionCard(
                    icon = Icons.Default.ViewDay,
                    title = "Home Feed Post",
                    subtitle = "Publish to main feed for all followers to view & like",
                    accentColor = SocialBrandBlue,
                    onClick = {
                        showDestinationPicker = false
                        val mediaString = selectedMediaUri?.toString() ?: selectedPresetImage
                        val newPost = Post(
                            id = "post_${System.currentTimeMillis()}",
                            author = SampleData.currentUser,
                            imageUrl = mediaString,
                            caption = caption.ifBlank { "Moments from today ✨ #minimal #lifestyle" },
                            location = location,
                            likesCount = 1,
                            commentsCount = 0,
                            timeAgo = "Just now",
                            isLiked = false,
                            isBookmarked = false
                        )
                        Toast.makeText(context, "Published to Home Feed!", Toast.LENGTH_SHORT).show()
                        onPostCreated(newPost)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Option 2: History / Story Post
                DestinationOptionCard(
                    icon = Icons.Default.History,
                    title = "History / Story Post",
                    subtitle = "Add to 24-hour active stories at the top of Home",
                    accentColor = Color(0xFFFF6B6B),
                    onClick = {
                        showDestinationPicker = false
                        val mediaString = selectedMediaUri?.toString() ?: selectedPresetImage
                        val newStory = Story(
                            id = "story_${System.currentTimeMillis()}",
                            user = SampleData.currentUser,
                            mediaUrl = mediaString,
                            hasUnseenStory = true,
                            timeAgo = "Just now"
                        )
                        Toast.makeText(context, "Added to Your Story!", Toast.LENGTH_SHORT).show()
                        onStoryCreated(newStory)
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Option 3: Reel Post
                DestinationOptionCard(
                    icon = Icons.Default.Movie,
                    title = "Reel Post",
                    subtitle = "Share as immersive vertical reel format with audio",
                    accentColor = Color(0xFF8B5CF6),
                    onClick = {
                        showDestinationPicker = false
                        val mediaString = selectedMediaUri?.toString() ?: selectedPresetImage
                        val newReel = Reel(
                            id = "reel_${System.currentTimeMillis()}",
                            creator = SampleData.currentUser,
                            videoThumbnailUrl = mediaString,
                            caption = caption.ifBlank { "Trending reel creation 🎬 #viral" },
                            audioTrackTitle = "Original Audio - ${SampleData.currentUser.username}",
                            likesCount = 1,
                            commentsCount = 0,
                            isLiked = false
                        )
                        Toast.makeText(context, "Published to Reels!", Toast.LENGTH_SHORT).show()
                        onReelCreated(newReel)
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun DestinationOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF9FAFB),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFECEEF2))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF8E8E93),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
