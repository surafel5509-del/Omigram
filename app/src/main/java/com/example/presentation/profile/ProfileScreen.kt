package com.example.presentation.profile

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.SampleData
import com.example.presentation.common.GridProIcon
import com.example.presentation.common.TaggedProIcon
import com.example.ui.theme.InstagramGradient
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText

import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Verified
import com.example.ui.theme.SocialAvatarRingCoral
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialPeachGradientBottom
import com.example.ui.theme.SocialPeachGradientMid
import com.example.ui.theme.SocialPeachGradientTop

data class StoryHighlight(val id: String, val title: String, val coverUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    showBackButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var isEditProfileSheetOpen by remember { mutableStateOf(false) }

    // Edit fields initialized from currentUser or fallback to Screenshot 4 persona
    var editName by remember(currentUser) { mutableStateOf(currentUser?.fullName ?: "Michael Anderson") }
    var editUsername by remember(currentUser) { mutableStateOf(currentUser?.username ?: "Michael.Anderson") }
    var editBio by remember(currentUser) { mutableStateOf(currentUser?.bio ?: "Product designer who focus on simplicity usability") }
    var editAvatarUrl by remember(currentUser) { mutableStateOf(currentUser?.avatarUrl ?: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&auto=format&fit=crop&q=80") }

    val userPosts = SampleData.samplePosts

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        topBar = {
            // Screenshot 4 Top Bar: Circular Back, @username, Circular Bell Notification
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SocialPeachGradientTop)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onNavigateBack() }
                        .testTag("profile_back_button"),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1A1A1A),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = "@$editUsername",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.testTag("profile_username_title")
                )

                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onNavigateToSettings() }
                        .testTag("profile_bell_button"),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF1A1A1A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SocialPeachGradientTop,
                            SocialPeachGradientMid,
                            SocialPeachGradientBottom
                        )
                    )
                )
        ) {
            // Screenshot 4: Profile Header Row (Avatar with coral-red ring + 3 Stats)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with coral red/orange ring (Screenshot 4)
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, SocialAvatarRingCoral, CircleShape)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = editAvatarUrl,
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                // 3 Stats in a horizontal row
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileStatColumn(count = "38", label = "Posts")
                    ProfileStatColumn(count = "4.2K", label = "followers")
                    ProfileStatColumn(count = "1,6K", label = "following")
                }
            }

            // Name & Bio Section with Verified Badge (Screenshot 4)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = editName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = "Verified",
                        tint = SocialBrandBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = editBio,
                    fontSize = 13.5.sp,
                    color = Color(0xFF4A4A4A),
                    lineHeight = 18.sp
                )
            }

            // Edit Profile Action Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Button(
                    onClick = { isEditProfileSheetOpen = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1A1A1A)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .border(1.dp, Color(0xFFE5E5EA), RoundedCornerShape(12.dp))
                        .testTag("edit_profile_button")
                ) {
                    Text("Edit Profile", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Screenshot 4: 4-Segment Pill Tab Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(46.dp),
                shape = CircleShape,
                color = Color(0xFFF2F3F6)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Segment 0: Grid (4 squares)
                    SegmentPillItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.GridOn,
                                contentDescription = "Grid",
                                tint = if (selectedTab == 0) Color(0xFF1A1A1A) else Color(0xFF8E8E93),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        testTag = "profile_tab_grid"
                    )

                    // Segment 1: Video / Play
                    SegmentPillItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.PlayCircle,
                                contentDescription = "Videos",
                                tint = if (selectedTab == 1) Color(0xFF1A1A1A) else Color(0xFF8E8E93),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        testTag = "profile_tab_video"
                    )

                    // Segment 2: Audio / Voice
                    SegmentPillItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Mic,
                                contentDescription = "Voice",
                                tint = if (selectedTab == 2) Color(0xFF1A1A1A) else Color(0xFF8E8E93),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        testTag = "profile_tab_voice"
                    )

                    // Segment 3: Tagged / Contact Card
                    SegmentPillItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.AccountBox,
                                contentDescription = "Tagged",
                                tint = if (selectedTab == 3) Color(0xFF1A1A1A) else Color(0xFF8E8E93),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        testTag = "profile_tab_tagged"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Posts Grid (3-column)
            val gridImages = if (selectedTab == 0) {
                listOf(
                    "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1518770660439-4636190af475?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&auto=format&fit=crop&q=80"
                )
            } else {
                listOf(
                    "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400&auto=format&fit=crop&q=80",
                    "https://images.unsplash.com/photo-1508921912186-1d1a45ebb3c1?w=400&auto=format&fit=crop&q=80"
                )
            }

            // Grid items chunked in rows of 3
            val rows = gridImages.chunked(3)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp)
            ) {
                rows.forEach { rowImages ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(1.5.dp)
                    ) {
                        rowImages.forEach { imgUrl ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(OmigramSecondaryBackground)
                            ) {
                                AsyncImage(
                                    model = imgUrl,
                                    contentDescription = "User post",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        // Pad empty spaces in row
                        for (i in 0 until (3 - rowImages.size)) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(1.5.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Edit Profile Modal Bottom Sheet
    if (isEditProfileSheetOpen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { isEditProfileSheetOpen = false },
            sheetState = sheetState,
            containerColor = OmigramBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { isEditProfileSheetOpen = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = OmigramPrimaryText)
                    }
                    Text("Edit profile", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OmigramPrimaryText)
                    TextButton(
                        onClick = {
                            viewModel.updateProfile(editName, editBio, "")
                            isEditProfileSheetOpen = false
                        }
                    ) {
                        Text("Done", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OmigramAccentBlue)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Avatar change
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = editAvatarUrl,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .border(1.dp, OmigramBorder, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Change profile photo",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OmigramAccentBlue,
                        modifier = Modifier.clickable {
                            editAvatarUrl = if (editAvatarUrl.contains("534528741775")) {
                                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&auto=format&fit=crop&q=80"
                            } else {
                                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80"
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = OmigramSecondaryBackground,
                        unfocusedContainerColor = OmigramSecondaryBackground,
                        focusedBorderColor = OmigramBorder,
                        unfocusedBorderColor = OmigramBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = editUsername,
                    onValueChange = { editUsername = it },
                    label = { Text("Username") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = OmigramSecondaryBackground,
                        unfocusedContainerColor = OmigramSecondaryBackground,
                        focusedBorderColor = OmigramBorder,
                        unfocusedBorderColor = OmigramBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = editBio,
                    onValueChange = { editBio = it },
                    label = { Text("Bio") },
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = OmigramSecondaryBackground,
                        unfocusedContainerColor = OmigramSecondaryBackground,
                        focusedBorderColor = OmigramBorder,
                        unfocusedBorderColor = OmigramBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun ProfileStatColumn(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF8E8E93)
        )
    }
}

@Composable
private fun SegmentPillItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(CircleShape)
            .then(
                if (selected) {
                    Modifier
                        .background(Color.White, CircleShape)
                        .border(0.5.dp, Color(0xFFE5E5EA), CircleShape)
                        .padding(horizontal = 24.dp)
                } else {
                    Modifier
                        .padding(horizontal = 16.dp)
                }
            )
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}
