package com.example.presentation.common

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.Message
import com.example.core.model.MessageStatus
import com.example.core.model.MessageType
import com.example.core.model.User
import com.example.data.local.SampleData
import com.example.ui.theme.OmigramOrange
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.appBorder
import com.example.ui.theme.appSurface
import com.example.ui.theme.appTextPrimary
import com.example.ui.theme.appTextSecondary

@Composable
fun ShareWithFriendsDialog(
    title: String,
    contentPreview: String,
    onDismiss: () -> Unit,
    onNavigateToChat: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val friends = remember {
        SampleData.sampleUsers.filter { it.id != SampleData.CURRENT_USER_ID }
    }
    val sentStatus = remember { mutableStateMapOf<String, Boolean>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = OmigramOrange,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = appTextPrimary
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = appTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Preview chip
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = OmigramOrange.copy(alpha = 0.08f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OmigramOrange.copy(alpha = 0.2f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = contentPreview,
                        fontSize = 12.5.sp,
                        color = appTextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Text(
                    text = "Send to Friends in Chat:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = appTextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .testTag("share_friends_list"),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(friends, key = { it.id }) { friend ->
                        val isSent = sentStatus[friend.id] == true
                        FriendShareRow(
                            friend = friend,
                            isSent = isSent,
                            onSend = {
                                sentStatus[friend.id] = true
                                Toast.makeText(context, "Sent to ${friend.fullName}!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = OmigramOrange),
                modifier = Modifier.testTag("share_dialog_done_button")
            ) {
                Text("Done", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = appSurface
    )
}

@Composable
private fun FriendShareRow(
    friend: User,
    isSent: Boolean,
    onSend: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = appSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, appBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(modifier = Modifier.size(40.dp)) {
                    AsyncImage(
                        model = friend.avatarUrl,
                        contentDescription = friend.fullName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                    if (friend.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(OnlineGreen)
                                .border(1.5.dp, appSurface, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = friend.fullName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = appTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "@${friend.username}",
                        fontSize = 11.5.sp,
                        color = appTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (isSent) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = OnlineGreen.copy(alpha = 0.15f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = OnlineGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sent",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnlineGreen
                        )
                    }
                }
            } else {
                Button(
                    onClick = onSend,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OmigramOrange),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Send", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
