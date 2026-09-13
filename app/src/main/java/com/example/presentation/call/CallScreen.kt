package com.example.presentation.call

import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.User
import com.example.data.local.SampleData
import com.example.ui.theme.OmigramOrange
import kotlinx.coroutines.delay

@Composable
fun CallScreen(
    userId: String,
    isVideoCall: Boolean,
    onEndCall: () -> Unit,
    isGroupCall: Boolean = false,
    groupName: String = "",
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
    var isRecording by remember { mutableStateOf(false) }

    // Play ringing tone upon launch and call pickup tone
    DisposableEffect(Unit) {
        val toneGen = try {
            ToneGenerator(AudioManager.STREAM_VOICE_CALL, 60)
        } catch (e: Exception) {
            null
        }
        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 300)
        onDispose {
            try {
                toneGen?.stopTone()
                toneGen?.release()
            } catch (_: Exception) {}
        }
    }

    // Call timing transition: Connecting -> Connected
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
        targetValue = 1.18f,
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

    // Speaking soundwave simulation
    val waveAnim1 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(400), RepeatMode.Reverse), label = "w1"
    )
    val waveAnim2 by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(350), RepeatMode.Reverse), label = "w2"
    )
    val waveAnim3 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(450), RepeatMode.Reverse), label = "w3"
    )

    val formattedDuration = remember(callDurationSeconds) {
        val minutes = callDurationSeconds / 60
        val seconds = callDurationSeconds % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    val displayTitle = if (isGroupCall) {
        if (groupName.isNotBlank()) groupName else "Group Call"
    } else {
        targetUser.fullName
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isVideoActive) listOf(
                        Color(0xFF0F172A),
                        Color(0xFF181528),
                        Color(0xFF0B0D14)
                    ) else listOf(
                        Color(0xFF26150D),
                        Color(0xFF1A1820),
                        Color(0xFF0F0F14)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("call_screen")
    ) {
        // Single user background video
        if (isVideoActive && !isGroupCall) {
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

        // Top Navigation & Security Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
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

            // Encrypted / Recording badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isRecording) Color(0xFFE5484D).copy(alpha = 0.25f) else Color.White.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, if (isRecording) Color(0xFFE5484D) else Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (isRecording) Color(0xFFE5484D) else Color(0xFF34C759))
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (isRecording) "REC  $formattedDuration" else if (isGroupCall) "Group (${if (isConnected) "4 active" else "connecting"})" else "End-to-end encrypted",
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isVideoActive) {
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
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
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            Toast.makeText(context, "Invite link copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Add participant",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // CENTER BODY: GROUP CALL GRID vs 1-ON-1 SINGLE VIEW
        if (isGroupCall) {
            val groupParticipants = listOf(
                Pair("You (Host)", SampleData.currentUser.avatarUrl),
                Pair(targetUser.fullName, targetUser.avatarUrl),
                Pair(SampleData.userJennifer.fullName, SampleData.userJennifer.avatarUrl),
                Pair(SampleData.userMarcus.fullName, SampleData.userMarcus.avatarUrl)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp, vertical = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = displayTitle,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (!isConnected) "Connecting group call..." else formattedDuration,
                    color = if (!isConnected) OmigramOrange else Color(0xFF34C759),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 2x2 Participants Grid
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GroupParticipantTile(
                            name = groupParticipants[0].first,
                            avatarUrl = groupParticipants[0].second,
                            isVideo = isVideoActive,
                            isMuted = isMuted,
                            isSpeaking = !isMuted,
                            waveHeight = waveAnim1,
                            modifier = Modifier.weight(1f)
                        )
                        GroupParticipantTile(
                            name = groupParticipants[1].first,
                            avatarUrl = groupParticipants[1].second,
                            isVideo = isVideoActive,
                            isMuted = false,
                            isSpeaking = true,
                            waveHeight = waveAnim2,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GroupParticipantTile(
                            name = groupParticipants[2].first,
                            avatarUrl = groupParticipants[2].second,
                            isVideo = isVideoActive,
                            isMuted = true,
                            isSpeaking = false,
                            waveHeight = 0.2f,
                            modifier = Modifier.weight(1f)
                        )
                        GroupParticipantTile(
                            name = groupParticipants[3].first,
                            avatarUrl = groupParticipants[3].second,
                            isVideo = isVideoActive,
                            isMuted = false,
                            isSpeaking = false,
                            waveHeight = waveAnim3,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        } else {
            // 1-on-1 Call View
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
                            .background(OmigramOrange.copy(alpha = pulseAlpha))
                    )
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .scale(pulseScale * 0.95f)
                            .clip(CircleShape)
                            .background(Color(0xFFFF9E7D).copy(alpha = pulseAlpha * 1.3f))
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

                if (isConnected) {
                    Spacer(Modifier.height(12.dp))
                    // Speaking soundwave bar indicators
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.width(4.dp).height((12 * waveAnim1).dp).clip(CircleShape).background(OmigramOrange))
                        Box(modifier = Modifier.width(4.dp).height((22 * waveAnim2).dp).clip(CircleShape).background(OmigramOrange))
                        Box(modifier = Modifier.width(4.dp).height((16 * waveAnim3).dp).clip(CircleShape).background(OmigramOrange))
                        Box(modifier = Modifier.width(4.dp).height((26 * waveAnim1).dp).clip(CircleShape).background(OmigramOrange))
                        Box(modifier = Modifier.width(4.dp).height((14 * waveAnim2).dp).clip(CircleShape).background(OmigramOrange))
                    }
                }
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
        }

        // Bottom Controls Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = Color.White.copy(alpha = 0.14f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
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

                    // Call Recording Toggle
                    CallControlButton(
                        icon = Icons.Filled.FiberManualRecord,
                        isActive = isRecording,
                        contentDescription = "Record Call",
                        onClick = {
                            isRecording = !isRecording
                            Toast.makeText(
                                context,
                                if (isRecording) "Recording started" else "Recording saved to device",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                    // End Call
                    Surface(
                        modifier = Modifier
                            .size(52.dp)
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
private fun GroupParticipantTile(
    name: String,
    avatarUrl: String,
    isVideo: Boolean,
    isMuted: Boolean,
    isSpeaking: Boolean,
    waveHeight: Float,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .aspectRatio(1.1f)
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = if (isSpeaking) 2.dp else 1.dp,
                color = if (isSpeaking) Color(0xFF34C759) else Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF1E1E26)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isVideo) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                }
            }

            // Dark bottom vignette
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
            )

            // Name and status overlay at bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (isMuted) {
                    Icon(
                        imageVector = Icons.Default.MicOff,
                        contentDescription = "Muted",
                        tint = Color(0xFFE5484D),
                        modifier = Modifier.size(14.dp)
                    )
                } else if (isSpeaking) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.width(2.5.dp).height((6 * waveHeight).dp).clip(CircleShape).background(Color(0xFF34C759)))
                        Box(modifier = Modifier.width(2.5.dp).height((12 * waveHeight).dp).clip(CircleShape).background(Color(0xFF34C759)))
                        Box(modifier = Modifier.width(2.5.dp).height((8 * waveHeight).dp).clip(CircleShape).background(Color(0xFF34C759)))
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
            .size(46.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = if (isActive) Color.White else Color.White.copy(alpha = 0.2f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isActive) Color(0xFF1E1B4B) else Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

