package com.example.presentation.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoSizeSelectActual
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.data.local.SampleData
import com.example.data.remote.supabase.SupabaseClientProvider
import com.example.presentation.common.FloatingPillNavBar
import com.example.presentation.common.SocialNavTab
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialPillDark
import com.example.ui.theme.SocialSoftCardBg
import com.example.ui.theme.SocialSoftIconBg
import com.example.ui.theme.SocialToggleGreen

enum class AppThemeMode {
    LIGHT,
    DARK,
    SYSTEM
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

    // Toggle states
    var pushNotificationEnabled by remember { mutableStateOf(true) }
    var directMessagesAlert by remember { mutableStateOf(true) }
    var callVibrationEnabled by remember { mutableStateOf(true) }

    var isPrivateAccount by remember { mutableStateOf(false) }
    var twoFactorEnabled by remember { mutableStateOf(false) }
    var readReceiptsEnabled by remember { mutableStateOf(true) }

    var highQualityUploads by remember { mutableStateOf(true) }
    var autoDownloadWifi by remember { mutableStateOf(true) }
    var cacheSizeMb by remember { mutableStateOf(146) }

    var selectedLanguage by remember { mutableStateOf("English") }
    var showLanguageDialog by remember { mutableStateOf(false) }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    val languages = listOf("English", "አማርኛ (Amharic)", "Español", "Français", "Deutsch", "العربية")

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(OmigramBackground)
            .testTag("settings_screen"),
        bottomBar = {
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
            // Top Bar
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
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.testTag("settings_title")
                )

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

            // Profile Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onNavigateToProfile() }
                    .testTag("settings_profile_card"),
                shape = RoundedCornerShape(24.dp),
                color = SocialSoftCardBg
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = SampleData.currentUser.avatarUrl,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = SampleData.currentUser.fullName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                        Text(
                            text = SampleData.currentUser.email,
                            fontSize = 13.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Go to Profile",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // SECTION 1: APPEARANCE & THEME (Light / Dark / System)
            SettingsSectionHeader(title = "Appearance & Theme", icon = Icons.Outlined.Palette)

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBECEF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "App Theme Mode",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Customize the interface style to match your preference",
                        fontSize = 12.sp,
                        color = Color(0xFF8E8E93),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemePillButton(
                            icon = Icons.Default.LightMode,
                            label = "Light",
                            isSelected = currentThemeMode == AppThemeMode.LIGHT,
                            onClick = { onThemeModeChange(AppThemeMode.LIGHT) },
                            modifier = Modifier.weight(1f)
                        )
                        ThemePillButton(
                            icon = Icons.Default.DarkMode,
                            label = "Dark",
                            isSelected = currentThemeMode == AppThemeMode.DARK,
                            onClick = { onThemeModeChange(AppThemeMode.DARK) },
                            modifier = Modifier.weight(1f)
                        )
                        ThemePillButton(
                            icon = Icons.Default.PhoneAndroid,
                            label = "System",
                            isSelected = currentThemeMode == AppThemeMode.SYSTEM,
                            onClick = { onThemeModeChange(AppThemeMode.SYSTEM) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 2: NOTIFICATIONS
            SettingsSectionHeader(title = "Notifications & Alerts", icon = Icons.Outlined.Notifications)

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBECEF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SettingsSwitchRow(
                        title = "Push Notifications",
                        subtitle = "Receive timely updates when friends interact with you",
                        isChecked = pushNotificationEnabled,
                        onCheckedChange = { pushNotificationEnabled = it }
                    )
                    HorizontalDivider(color = Color(0xFFF0F1F3))
                    SettingsSwitchRow(
                        title = "Direct Messages Alert",
                        subtitle = "Alert tones for new incoming chats",
                        isChecked = directMessagesAlert,
                        onCheckedChange = { directMessagesAlert = it }
                    )
                    HorizontalDivider(color = Color(0xFFF0F1F3))
                    SettingsSwitchRow(
                        title = "Call Ringtone & Vibrate",
                        subtitle = "Vibrate on voice and video calls",
                        isChecked = callVibrationEnabled,
                        onCheckedChange = { callVibrationEnabled = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 3: PRIVACY & SECURITY
            SettingsSectionHeader(title = "Privacy & Security", icon = Icons.Outlined.Security)

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBECEF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SettingsSwitchRow(
                        title = "Private Account",
                        subtitle = "Only approved connections can see your posts and stories",
                        isChecked = isPrivateAccount,
                        onCheckedChange = {
                            isPrivateAccount = it
                            Toast.makeText(context, if (it) "Account set to Private" else "Account set to Public", Toast.LENGTH_SHORT).show()
                        }
                    )
                    HorizontalDivider(color = Color(0xFFF0F1F3))
                    SettingsSwitchRow(
                        title = "Read Receipts",
                        subtitle = "Let others know when you have seen their messages",
                        isChecked = readReceiptsEnabled,
                        onCheckedChange = { readReceiptsEnabled = it }
                    )
                    HorizontalDivider(color = Color(0xFFF0F1F3))
                    SettingsSwitchRow(
                        title = "Two-Factor Authentication",
                        subtitle = "Add an extra layer of protection to your account",
                        isChecked = twoFactorEnabled,
                        onCheckedChange = {
                            twoFactorEnabled = it
                            Toast.makeText(context, if (it) "2FA Enabled" else "2FA Disabled", Toast.LENGTH_SHORT).show()
                        }
                    )
                    HorizontalDivider(color = Color(0xFFF0F1F3))
                    SettingsNavigationRow(
                        title = "Change Password",
                        subtitle = "Update your credentials securely",
                        onClick = { showChangePasswordDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 4: STORAGE & MEDIA
            SettingsSectionHeader(title = "Storage & Media Quality", icon = Icons.Outlined.Storage)

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBECEF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    SettingsSwitchRow(
                        title = "Full HD Media Uploads",
                        subtitle = "Upload photos and videos in pristine uncompressed resolution",
                        isChecked = highQualityUploads,
                        onCheckedChange = { highQualityUploads = it }
                    )
                    HorizontalDivider(color = Color(0xFFF0F1F3))
                    SettingsSwitchRow(
                        title = "Auto-Download via Wi-Fi",
                        subtitle = "Conserve mobile data for videos and images",
                        isChecked = autoDownloadWifi,
                        onCheckedChange = { autoDownloadWifi = it }
                    )
                    HorizontalDivider(color = Color(0xFFF0F1F3))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                cacheSizeMb = 0
                                Toast.makeText(context, "App cache cleared successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Clear Cached Data",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = "Free up storage space without losing personal messages",
                                fontSize = 12.sp,
                                color = Color(0xFF8E8E93)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF4F5F7)
                        ) {
                            Text(
                                text = if (cacheSizeMb > 0) "$cacheSizeMb MB" else "Cleared",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (cacheSizeMb > 0) SocialBrandBlue else Color(0xFF34C759),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 5: LANGUAGE & REGION
            SettingsSectionHeader(title = "Language & Region", icon = Icons.Outlined.Language)

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBECEF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsNavigationRow(
                    title = "App Language",
                    subtitle = selectedLanguage,
                    onClick = { showLanguageDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 6: CLOUD SYNC & APP INFO
            SettingsSectionHeader(title = "Cloud & System Info", icon = Icons.Outlined.CloudDone)

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEBECEF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Omigram Cloud Infrastructure",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A1A1A)
                            )
                            Text(
                                text = if (SupabaseClientProvider.isConfigured) "Connected to Supabase Project" else "Local & Cloud Sync Active",
                                fontSize = 12.sp,
                                color = Color(0xFF8E8E93)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF34C759))
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFF0F1F3))
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Version",
                            fontSize = 13.sp,
                            color = Color(0xFF8E8E93)
                        )
                        Text(
                            text = "v2.4.0 (Production Release)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Logout Button
            Button(
                onClick = { showLogoutDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFF0F0)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = Color(0xFFFF3B30),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Sign Out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF3B30)
                )
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    // Language Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Choose App Language", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    languages.forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLanguage = lang
                                    showLanguageDialog = false
                                    Toast.makeText(context, "Language updated to $lang", Toast.LENGTH_SHORT).show()
                                }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = lang, fontSize = 15.sp, color = Color(0xFF1A1A1A))
                            if (selectedLanguage == lang) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = SocialBrandBlue)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text("Change Password", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
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
                    colors = ButtonDefaults.buttonColors(containerColor = SocialPillDark)
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to sign out from Omigram?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
                ) {
                    Text("Sign Out", color = Color.White)
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
private fun SettingsSectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF1A1A1A),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )
    }
}

@Composable
private fun ThemePillButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(44.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) SocialPillDark else Color(0xFFF4F5F7),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) SocialPillDark else Color(0xFFE5E5EA)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color(0xFF1A1A1A),
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF1A1A1A)
            )
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF8E8E93),
                lineHeight = 16.sp
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SocialToggleGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE5E5EA)
            )
        )
    }
}

@Composable
private fun SettingsNavigationRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF8E8E93)
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF8E8E93),
            modifier = Modifier.size(20.dp)
        )
    }
}
