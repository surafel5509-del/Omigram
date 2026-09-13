package com.example.presentation.call

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.User
import com.example.data.local.SampleData
import kotlinx.coroutines.delay

@Composable
fun CallScreen(
    userId: String,
    isVideoCall: Boolean,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val targetUser: User = remember(userId) {
        SampleData.sampleUsers.find { it.id == userId }
            ?: SampleData.sampleUsers.firstOrNull { it.id != SampleData.CURRENT_USER_ID }
            ?: SampleData.currentUser
    }

    var callDurationSeconds by remember { mutableIntStateOf(0) }
    var isConnected by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(isVideoCall) }
    var isVideoActive by remember { mutableStateOf(isVideoCall) }
    var isFrontCamera by remember { mutableStateOf(true) }

    // Call timing transition: Connecting -> Ringing -> Connected
    LaunchedEffect(Unit) {
        delay(1200)
        isConnected = true
        while (true) {
            delay(1000)
            callDurationSeconds++
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val formattedDuration = remember(callDurationSeconds) {
        val minutes = callDurationSeconds / 60
        val seconds = callDurationSeconds % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isVideoActive) listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF0F172A)
                    ) else listOf(
                        Color(0xFF2C1810),
                        Color(0xFF1E1E24),
                        Color(0xFF121216)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("call_screen")
    ) {
        // Full screen video background if Video is active
        if (isVideoActive) {
            AsyncImage(
                model = targetUser.avatarUrl,
                contentDescription = "Video Feed",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.35f)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.75f)
                            )
                        )
                    )
            )
        }

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(42.dp)
                    .clickable { onEndCall() }
                    .testTag("call_back_button"),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Secure Encryption Pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.12f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF34C759))
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "End-to-end encrypted",
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (isVideoActive) {
                Surface(
                    modifier = Modifier
                        .size(42.dp)
                        .clickable {
                            isFrontCamera = !isFrontCamera
                            Toast.makeText(
                                context,
                                if (isFrontCamera) "Front camera" else "Back camera",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .testTag("call_flip_camera_button"),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Cameraswitch,
                            contentDescription = "Flip camera",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                Spacer(Modifier.size(42.dp))
            }
        }

        // Center Content (Caller info & Avatar)
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(180.dp)
            ) {
                // Pulsing rings
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(Color(0xFFFF9E7D).copy(alpha = pulseAlpha))
                )
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(pulseScale * 0.95f)
                        .clip(CircleShape)
                        .background(Color(0xFF3B82F6).copy(alpha = pulseAlpha * 1.3f))
                )

                // Main Avatar
                AsyncImage(
                    model = targetUser.avatarUrl,
                    contentDescription = targetUser.fullName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(114.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = targetUser.fullName,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = if (!isConnected) "Connecting..." else formattedDuration,
                color = if (!isConnected) Color(0xFFFFB299) else Color(0xFF34C759),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Picture-in-picture Self View if Video Active
        if (isVideoActive) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 80.dp, end = 20.dp)
                    .size(width = 96.dp, height = 138.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black
            ) {
                AsyncImage(
                    model = SampleData.currentUser.avatarUrl,
                    contentDescription = "My Camera Preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Bottom Controls Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = Color.White.copy(alpha = 0.14f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mute
                    CallControlButton(
                        icon = if (isMuted) Icons.Filled.MicOff else Icons.Filled.Mic,
                        isActive = isMuted,
                        contentDescription = "Mute Microphone",
                        onClick = {
                            isMuted = !isMuted
                            Toast.makeText(context, if (isMuted) "Microphone muted" else "Microphone unmuted", Toast.LENGTH_SHORT).show()
                        }
                    )

                    // Video Toggle
                    CallControlButton(
                        icon = if (isVideoActive) Icons.Filled.Videocam else Icons.Filled.VideocamOff,
                        isActive = isVideoActive,
                        contentDescription = "Toggle Video",
                        onClick = {
                            isVideoActive = !isVideoActive
                        }
                    )

                    // Speaker
                    CallControlButton(
                        icon = if (isSpeakerOn) Icons.Filled.VolumeUp else Icons.Filled.VolumeDown,
                        isActive = isSpeakerOn,
                        contentDescription = "Speakerphone",
                        onClick = {
                            isSpeakerOn = !isSpeakerOn
                            Toast.makeText(context, if (isSpeakerOn) "Speaker on" else "Speaker off", Toast.LENGTH_SHORT).show()
                        }
                    )

                    // End Call
                    Surface(
                        modifier = Modifier
                            .size(54.dp)
                            .clickable {
                                Toast.makeText(context, "Call ended ($formattedDuration)", Toast.LENGTH_SHORT).show()
                                onEndCall()
                            }
                            .testTag("end_call_button"),
                        shape = CircleShape,
                        color = Color(0xFFFF3B30)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.CallEnd,
                                contentDescription = "End call",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CallControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(50.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = if (isActive) Color.White else Color.White.copy(alpha = 0.2f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isActive) Color(0xFF1E1B4B) else Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

private fun Modifier.alpha(alpha: Float): Modifier = this.then(
    Modifier.background(Color.Transparent)
)
