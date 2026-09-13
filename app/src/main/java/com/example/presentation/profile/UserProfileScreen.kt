package com.example.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.local.SampleData
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.SocialPeachGradientBottom
import com.example.ui.theme.SocialPeachGradientMid
import com.example.ui.theme.SocialPeachGradientTop

@Composable
fun UserProfileScreen(
    userId: String,
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user = SampleData.sampleUsers.find { it.id == userId } ?: SampleData.userJennifer

    var isPrivateMode by remember { mutableStateOf(false) }
    var activeCallType by remember { mutableStateOf<String?>(null) }

    val userPhotos = listOf(
        "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=400&auto=format&fit=crop&q=80"
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("user_profile_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(OmigramBackground)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Screenshot 2 Header: Badge "2", Title "Personalized Profiles", Subtitle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                // Step Indicator Badge "2"
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .shadow(4.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.06f)),
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "2",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Personalized Profiles",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Add a personal touch with avatars, greetings, and relevant actions.",
                    fontSize = 14.sp,
                    color = Color(0xFF74747C),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Screenshot 2 Floating Card with warm peach gradient
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(32.dp),
                        spotColor = Color(0xFFD4A373).copy(alpha = 0.25f),
                        ambientColor = Color.Black.copy(alpha = 0.05f)
                    )
                    .testTag("user_profile_card"),
                shape = RoundedCornerShape(32.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFBECE4),
                                    Color(0xFFFBF4EF),
                                    Color(0xFFFAF6F2)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Top Navigation Bar inside card: Back on left, Share & Link on right
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular Back Button
                            Surface(
                                modifier = Modifier
                                    .size(42.dp)
                                    .shadow(4.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.08f)),
                                shape = CircleShape,
                                color = Color.White
                            ) {
                                IconButton(
                                    onClick = onNavigateBack,
                                    modifier = Modifier.testTag("user_profile_back_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color(0xFF1A1A1A),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                // Circular Share Button
                                Surface(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .shadow(4.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.08f)),
                                    shape = CircleShape,
                                    color = Color.White
                                ) {
                                    IconButton(
                                        onClick = {
                                            Toast.makeText(context, "Profile shared!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.testTag("user_profile_share_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Share,
                                            contentDescription = "Share Profile",
                                            tint = Color(0xFF1A1A1A),
                                            modifier = Modifier.size(19.dp)
                                        )
                                    }
                                }

                                // Circular Link Button
                                Surface(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .shadow(4.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.08f)),
                                    shape = CircleShape,
                                    color = Color.White
                                ) {
                                    IconButton(
                                        onClick = {
                                            Toast.makeText(context, "Profile link copied to clipboard!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.testTag("user_profile_link_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Link,
                                            contentDescription = "Copy Link",
                                            tint = Color(0xFF1A1A1A),
                                            modifier = Modifier.size(19.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Center Avatar with Online Status Indicator
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier.size(96.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = user.avatarUrl,
                                    contentDescription = user.fullName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(92.dp)
                                        .clip(CircleShape)
                                        .border(2.5.dp, Color.White, CircleShape)
                                )

                                // Green Online Indicator Dot
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .align(Alignment.BottomEnd)
                                        .clip(CircleShape)
                                        .background(Color(0xFF34C759))
                                        .border(2.5.dp, Color.White, CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Name
                            Text(
                                text = user.fullName,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A),
                                modifier = Modifier.testTag("user_profile_name")
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Last seen recently
                            Text(
                                text = if (user.isOnline) "Active now" else "Last seen recently",
                                fontSize = 13.sp,
                                color = Color(0xFF8E8E93),
                                modifier = Modifier.testTag("user_profile_last_seen")
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Screenshot 2: 4 Squircle Action Buttons in a horizontal row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Button 1: Message
                            SquircleActionButton(
                                icon = Icons.Outlined.ChatBubbleOutline,
                                label = "Message",
                                onClick = {
                                    val chat = SampleData.sampleChats.find { it.participant.id == user.id }
                                    if (chat != null) onNavigateToChat(chat.id)
                                    else if (SampleData.sampleChats.isNotEmpty()) onNavigateToChat(SampleData.sampleChats.first().id)
                                },
                                testTag = "action_btn_message"
                            )

                            // Button 2: Call
                            SquircleActionButton(
                                icon = Icons.Filled.Call,
                                label = "Call",
                                onClick = { activeCallType = "Voice Call" },
                                testTag = "action_btn_call"
                            )

                            // Button 3: Video call
                            SquircleActionButton(
                                icon = Icons.Filled.Videocam,
                                label = "Video call",
                                onClick = { activeCallType = "Video Call" },
                                testTag = "action_btn_video"
                            )

                            // Button 4: Private
                            SquircleActionButton(
                                icon = Icons.Filled.Lock,
                                label = if (isPrivateMode) "Secured" else "Private",
                                onClick = {
                                    isPrivateMode = !isPrivateMode
                                    val status = if (isPrivateMode) "Private mode enabled" else "Private mode disabled"
                                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                                },
                                iconTint = if (isPrivateMode) Color(0xFF34C759) else Color(0xFF1A1A1A),
                                testTag = "action_btn_private"
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Screenshot 2: Bio Section Card inside the Peach container
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.04f)),
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.9f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)
                            ) {
                                Text(
                                    text = "bio",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF8E8E93)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (user.fullName.contains("Jennifer", ignoreCase = true)) "Mother of Maggie and Maral." else user.bio,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF1A1A1A),
                                    lineHeight = 22.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Screenshot 2 Bottom Navigation Indicator Row (Bookmark left, Arrow right)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(38.dp)
                                    .shadow(2.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.05f)),
                                shape = CircleShape,
                                color = Color.White
                            ) {
                                IconButton(onClick = {
                                    Toast.makeText(context, "Saved to bookmarks", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Bookmark,
                                        contentDescription = "Bookmark",
                                        tint = Color(0xFF1A1A1A),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .size(38.dp)
                                    .shadow(2.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.05f)),
                                shape = CircleShape,
                                color = Color.White
                            ) {
                                IconButton(onClick = {
                                    Toast.makeText(context, "Next profile", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Next",
                                        tint = Color(0xFF1A1A1A),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Media / Photos Gallery
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Shared Media",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    userPhotos.take(3).forEach { photoUrl ->
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Media item",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFFEBECEF), RoundedCornerShape(16.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Call Dialog
    if (activeCallType != null) {
        AlertDialog(
            onDismissRequest = { activeCallType = null },
            title = {
                Text("$activeCallType with ${user.fullName}")
            },
            text = {
                Text("Calling ${user.fullName}... (High Definition End-to-End Encrypted)")
            },
            confirmButton = {
                Button(
                    onClick = { activeCallType = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED4956))
                ) {
                    Text("End Call", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { activeCallType = null }) {
                    Text("Minimize")
                }
            }
        )
    }
}

@Composable
private fun SquircleActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier,
    iconTint: Color = Color(0xFF1A1A1A)
) {
    Surface(
        modifier = modifier
            .width(78.dp)
            .height(68.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.04f))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF7EFE9).copy(alpha = 0.75f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEFE6DF))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A1A)
            )
        }
    }
}
