package com.example.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.core.model.Post
import com.example.core.model.Story
import com.example.presentation.common.CommentsSheet
import com.example.presentation.common.CreateProIcon
import com.example.presentation.common.DmProIcon
import com.example.presentation.common.HomeProIcon
import com.example.presentation.common.OmigramLogo
import com.example.presentation.common.PostItemCard
import com.example.presentation.common.ReelsProIcon
import com.example.presentation.common.SearchProIcon
import com.example.presentation.common.StoryViewerDialog
import com.example.presentation.create.CreateScreen
import com.example.presentation.explore.ExploreScreen
import com.example.presentation.profile.ProfileScreen
import com.example.presentation.profile.ProfileViewModel
import com.example.presentation.reels.ReelsScreen
import com.example.ui.theme.InstagramGradient
import com.example.ui.theme.OmigramAccentBlue
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramErrorRed
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText

import androidx.compose.material.icons.outlined.Search
import com.example.presentation.common.FloatingPillNavBar
import com.example.presentation.common.SocialNavTab
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialSoftIconBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    onNavigateToChat: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToUserProfile: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val posts by viewModel.posts.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val chats by viewModel.chats.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val totalUnreadMessages = chats.sumOf { it.unreadCount }

    // Dialogs / Sheets state
    var viewingStoryIndex by remember { mutableStateOf<Int?>(null) }
    var activePostForComments by remember { mutableStateOf<Post?>(null) }
    var isCreatePostOpen by remember { mutableStateOf(false) }

    val currentPillTab = when (uiState.selectedTab) {
        OmigramTab.HOME -> SocialNavTab.HOME
        OmigramTab.REELS -> SocialNavTab.REELS
        OmigramTab.PROFILE -> SocialNavTab.FRIENDS
        else -> SocialNavTab.HOME
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(OmigramBackground)
            .testTag("home_screen"),
        topBar = {
            if (uiState.selectedTab == OmigramTab.HOME) {
                // Screenshot 1 Header: "facebook" logo on left, circular Search, Messenger, Avatar buttons on right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OmigramBackground)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "facebook",
                        color = SocialBrandBlue,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.testTag("home_brand_logo")
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular Search Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SocialSoftIconBg)
                                .clickable { viewModel.selectTab(OmigramTab.SEARCH) }
                                .testTag("top_bar_search_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                tint = OmigramPrimaryText,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Circular Messenger Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SocialSoftIconBg)
                                .clickable { onNavigateToMessages() }
                                .testTag("top_bar_messages_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            BadgedBox(
                                badge = {
                                    if (totalUnreadMessages > 0) {
                                        Badge(
                                            containerColor = SocialBrandBlue,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = totalUnreadMessages.toString(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            ) {
                                DmProIcon(
                                    tint = OmigramPrimaryText,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Circular User Avatar Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, OmigramBorder, CircleShape)
                                .clickable { viewModel.selectTab(OmigramTab.PROFILE) }
                                .testTag("top_bar_profile_avatar_button")
                        ) {
                            AsyncImage(
                                model = currentUser?.avatarUrl ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
                                contentDescription = "My Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Screenshot 1 Floating Glassmorphic Pill Navigation Bar
            FloatingPillNavBar(
                selectedTab = currentPillTab,
                onTabSelected = { tab ->
                    when (tab) {
                        SocialNavTab.HOME -> viewModel.selectTab(OmigramTab.HOME)
                        SocialNavTab.REELS -> viewModel.selectTab(OmigramTab.REELS)
                        SocialNavTab.NOTIFICATIONS -> onNavigateToNotifications()
                        SocialNavTab.FRIENDS -> viewModel.selectTab(OmigramTab.PROFILE)
                        SocialNavTab.SETTINGS -> onNavigateToSettings()
                    }
                },
                onActionButtonClick = { isCreatePostOpen = true },
                actionButtonIcon = Icons.Default.Add,
                actionButtonColor = SocialBrandBlue,
                actionButtonTag = "floating_add_post_button"
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(OmigramBackground)
        ) {
            when (uiState.selectedTab) {
                OmigramTab.HOME -> {
                    FeedTabContent(
                        stories = stories,
                        posts = posts,
                        onStoryClick = { index -> viewingStoryIndex = index },
                        onLikeToggle = { postId -> viewModel.toggleLikePost(postId) },
                        onCommentClick = { post -> activePostForComments = post },
                        onShareClick = { onNavigateToMessages() },
                        onSaveToggle = { postId -> viewModel.toggleSavePost(postId) },
                        onUserClick = { userId -> onNavigateToUserProfile(userId) }
                    )
                }
                OmigramTab.SEARCH -> {
                    ExploreScreen(
                        onNavigateToUserProfile = onNavigateToUserProfile
                    )
                }
                OmigramTab.CREATE -> {
                    CreateScreen(
                        onDismiss = { viewModel.selectTab(OmigramTab.HOME) },
                        onPostCreated = { newPost ->
                            viewModel.addNewPost(newPost)
                        }
                    )
                }
                OmigramTab.REELS -> {
                    ReelsScreen(
                        onNavigateToUserProfile = onNavigateToUserProfile
                    )
                }
                OmigramTab.PROFILE -> {
                    ProfileScreen(
                        viewModel = profileViewModel,
                        onNavigateToSettings = onNavigateToSettings,
                        showBackButton = false
                    )
                }
            }
        }
    }

    // Modal Create Post Flow (when plus icon is tapped)
    if (isCreatePostOpen) {
        CreateScreen(
            onDismiss = { isCreatePostOpen = false },
            onPostCreated = { post ->
                viewModel.addNewPost(post)
                isCreatePostOpen = false
            }
        )
    }

    // Interactive Fullscreen Story Viewer
    if (viewingStoryIndex != null) {
        StoryViewerDialog(
            stories = stories,
            initialStoryIndex = viewingStoryIndex!!,
            onDismiss = { viewingStoryIndex = null }
        )
    }

    // Interactive Comments Bottom Sheet
    if (activePostForComments != null) {
        CommentsSheet(
            post = activePostForComments!!,
            onDismiss = { activePostForComments = null }
        )
    }
}

@Composable
private fun FeedTabContent(
    stories: List<Story>,
    posts: List<Post>,
    onStoryClick: (Int) -> Unit,
    onLikeToggle: (String) -> Unit,
    onCommentClick: (Post) -> Unit,
    onShareClick: (Post) -> Unit,
    onSaveToggle: (String) -> Unit,
    onUserClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("feed_list")
    ) {
        // Stories Tray
        item {
            StoriesTray(
                stories = stories,
                onStoryClick = onStoryClick
            )
            HorizontalDivider(color = OmigramBorder, thickness = 0.5.dp)
        }

        // Feed Posts
        items(posts, key = { it.id }) { post ->
            PostItemCard(
                post = post,
                onLikeToggle = { onLikeToggle(post.id) },
                onCommentClick = { onCommentClick(post) },
                onShareClick = { onShareClick(post) },
                onSaveToggle = { onSaveToggle(post.id) },
                onUserClick = { onUserClick(post.author.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StoriesTray(
    stories: List<Story>,
    onStoryClick: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .testTag("stories_tray"),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // "Add story" card matching Screenshot 1
        item {
            Box(
                modifier = Modifier
                    .width(104.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEDF4FF))
                    .clickable { onStoryClick(0) }
                    .testTag("add_story_card"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(2.dp, SocialBrandBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add story",
                            tint = SocialBrandBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Add story",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = SocialBrandBlue
                    )
                }
            }
        }

        // Story Cards with full-bleed image and bottom avatar + info
        itemsIndexed(stories) { index, story ->
            Box(
                modifier = Modifier
                    .width(104.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(OmigramSecondaryBackground)
                    .clickable { onStoryClick(index) }
                    .testTag("story_card_${story.id}")
            ) {
                // Background story image
                AsyncImage(
                    model = story.mediaUrl,
                    contentDescription = story.user.fullName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient scrim overlay at the bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.78f)
                                )
                            )
                        )
                )

                // Bottom user avatar and info row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = story.user.avatarUrl,
                        contentDescription = story.user.fullName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Column {
                        Text(
                            text = story.user.fullName.ifEmpty { story.user.username },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = story.timeAgo,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
