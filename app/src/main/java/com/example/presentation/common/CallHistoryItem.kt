package com.example.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.model.Call
import com.example.core.model.CallStatus
import com.example.core.model.CallType
import com.example.data.local.SampleData
import com.example.ui.theme.MissedCallRed
import com.example.ui.theme.OnlineGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CallHistoryItem(
    call: Call,
    onCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isMeCaller = call.caller.id == SampleData.CURRENT_USER_ID
    val otherUser = if (isMeCaller) call.receiver else call.caller

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxWidth()
            .testTag("call_item_${call.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                imageUrl = otherUser.avatarUrl,
                name = otherUser.fullName,
                size = 50.dp,
                isOnline = otherUser.isOnline
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = otherUser.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (call.status == CallStatus.MISSED) MissedCallRed else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.padding(top = 2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val (directionIcon, tint) = when (call.status) {
                        CallStatus.MISSED -> Icons.AutoMirrored.Filled.CallMissed to MissedCallRed
                        CallStatus.INCOMING -> Icons.AutoMirrored.Filled.CallReceived to OnlineGreen
                        CallStatus.OUTGOING -> Icons.AutoMirrored.Filled.CallMade to MaterialTheme.colorScheme.primary
                    }

                    Icon(
                        imageVector = directionIcon,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(15.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    val timeStr = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(call.startedAt))
                    Text(
                        text = timeStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = onCallClick,
                modifier = Modifier.testTag("call_action_button_${call.id}")
            ) {
                Icon(
                    imageVector = if (call.callType == CallType.VIDEO) Icons.Default.Videocam else Icons.Default.Call,
                    contentDescription = "Call ${otherUser.fullName}",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
