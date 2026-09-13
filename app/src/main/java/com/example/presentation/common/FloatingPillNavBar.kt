package com.example.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialPillDark

/** Shared floating navigation used across Omigram's social surfaces. */
enum class SocialNavTab(
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val testTag: String
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "pill_tab_home"),
    REELS("Explore", Icons.Filled.Explore, Icons.Outlined.Explore, "pill_tab_reels"),
    NOTIFICATIONS("Likes", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "pill_tab_notifications"),
    FRIENDS("Profile", Icons.Filled.Person, Icons.Outlined.Person, "pill_tab_friends"),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "pill_tab_settings")
}

@Composable
fun FloatingPillNavBar(
    selectedTab: SocialNavTab,
    onTabSelected: (SocialNavTab) -> Unit,
    onActionButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionButtonIcon: ImageVector = Icons.Filled.Search,
    actionButtonColor: Color = SocialBrandBlue,
    actionButtonTag: String = "pill_action_button"
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        spotColor = Color.Black.copy(alpha = 0.08f),
                        ambientColor = Color.Black.copy(alpha = 0.04f)
                    )
                    .testTag("floating_pill_nav_bar"),
                shape = CircleShape,
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFEBECEF)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val tabsToDisplay = if (selectedTab == SocialNavTab.SETTINGS) {
                        listOf(
                            SocialNavTab.HOME,
                            SocialNavTab.REELS,
                            SocialNavTab.NOTIFICATIONS,
                            SocialNavTab.SETTINGS
                        )
                    } else {
                        listOf(
                            SocialNavTab.HOME,
                            SocialNavTab.REELS,
                            SocialNavTab.NOTIFICATIONS,
                            SocialNavTab.FRIENDS
                        )
                    }

                    tabsToDisplay.forEach { tab ->
                        val selected = selectedTab == tab
                        if (selected) {
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (tab == SocialNavTab.SETTINGS) Color(0xFFECEEF2) else SocialPillDark)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { onTabSelected(tab) }
                                    .padding(horizontal = 14.dp, vertical = 9.dp)
                                    .testTag(tab.testTag),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = tab.filledIcon,
                                    contentDescription = tab.title,
                                    tint = if (tab == SocialNavTab.SETTINGS) Color(0xFF17171A) else Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = tab.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (tab == SocialNavTab.SETTINGS) Color(0xFF17171A) else Color.White
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { onTabSelected(tab) }
                                    .testTag(tab.testTag),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tab.outlinedIcon,
                                    contentDescription = tab.title,
                                    tint = Color(0xFF26262B),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            // Vibrant Blue circular action button (Screenshot 1)
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = actionButtonColor.copy(alpha = 0.35f)
                    )
                    .clip(CircleShape)
                    .background(actionButtonColor)
                    .clickable { onActionButtonClick() }
                    .testTag(actionButtonTag),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = actionButtonIcon,
                    contentDescription = "Action",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
