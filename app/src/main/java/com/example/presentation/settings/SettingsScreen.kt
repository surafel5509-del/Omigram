package com.example.presentation.settings

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.presentation.common.FloatingPillNavBar
import com.example.presentation.common.SocialNavTab
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialSoftCardBg
import com.example.ui.theme.SocialSoftIconBg
import com.example.ui.theme.SocialToggleGreen

enum class AppThemeMode {
    LIGHT
}

@Composable
fun SettingsScreen(
    currentThemeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var pushNotificationEnabled by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(OmigramBackground)
            .testTag("settings_screen"),
        bottomBar = {
            // Floating Pill Nav Bar with Settings tab active and Search action button (Screenshot 2)
            FloatingPillNavBar(
                selectedTab = SocialNavTab.SETTINGS,
                onTabSelected = { tab ->
                    when (tab) {
                        SocialNavTab.HOME -> onNavigateBack()
                        SocialNavTab.FRIENDS -> onNavigateToProfile()
                        SocialNavTab.SETTINGS -> { /* already here */ }
                        else -> onNavigateBack()
                    }
                },
                onActionButtonClick = {
                    Toast.makeText(context, "Search settings or people", Toast.LENGTH_SHORT).show()
                },
                actionButtonIcon = Icons.Outlined.Search,
                actionButtonColor = SocialBrandBlue,
                actionButtonTag = "settings_action_search"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Screenshot 2 Top Bar: "Settings" title on left, Circular Logout on right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.testTag("settings_title")
                )

                // Circular Logout button
                Surface(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFFE5E5EA), CircleShape)
                        .clickable { showLogoutDialog = true }
                        .testTag("settings_logout_button"),
                    shape = CircleShape,
                    color = SocialSoftIconBg
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color(0xFF1A1A1A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Screenshot 2 Profile Card: Avatar on left, Michael Anderson / email on right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&auto=format&fit=crop&q=80",
                    contentDescription = "Michael Anderson",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFFE5E5EA), CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Michael Anderson",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "michael.anderson@gmail.com",
                        fontSize = 13.sp,
                        color = Color(0xFF8E8E93)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Screenshot 2 Menu Option 1: User Profile
            SettingsCardItem(
                icon = Icons.Outlined.Person,
                title = "User Profile",
                onClick = onNavigateToProfile,
                testTag = "settings_item_user_profile"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Screenshot 2 Menu Option 2: Change Password
            SettingsCardItem(
                icon = Icons.Outlined.Lock,
                title = "Change Password",
                onClick = { showChangePasswordDialog = true },
                testTag = "settings_item_change_password"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Screenshot 2 Menu Option 3: Push Notification with green toggle
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, Color(0xFFECEEF2), RoundedCornerShape(18.dp))
                    .testTag("settings_item_push_notification"),
                shape = RoundedCornerShape(18.dp),
                color = SocialSoftCardBg
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Push Notification",
                            tint = Color(0xFF1A1A1A),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = "Push Notification",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1A1A1A)
                        )
                    }

                    Switch(
                        checked = pushNotificationEnabled,
                        onCheckedChange = { pushNotificationEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = SocialToggleGreen,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD1D1D6)
                        ),
                        modifier = Modifier.testTag("push_notification_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = {
                Text("Change Password", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        "Enter your current and new password to keep your account safe.",
                        fontSize = 13.sp,
                        color = Color(0xFF8E8E93)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPassword.isNotBlank()) {
                            Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                            showChangePasswordDialog = false
                            currentPassword = ""
                            newPassword = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SocialBrandBlue)
                ) {
                    Text("Update", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text("Log out of account?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("You can log back in at any time with your credentials.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED4956))
                ) {
                    Text("Log Out", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsCardItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFECEEF2), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        color = SocialSoftCardBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF1A1A1A),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF8E8E93),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
