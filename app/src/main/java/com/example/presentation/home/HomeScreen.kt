package com.example.presentation.home

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.presentation.common.DmProIcon
import com.example.presentation.common.FloatingPillNavBar
import com.example.presentation.common.PostItemCard
import com.example.presentation.common.SocialNavTab
import com.example.presentation.common.StoryViewerDialog
import com.example.presentation.create.CreateScreen
import com.example.presentation.explore.ExploreScreen
import com.example.presentation.profile.ProfileScreen
import com.example.presentation.profile.ProfileViewModel
import com.example.presentation.reels.ReelsScreen
import com.example.ui.theme.OmigramBackground
import com.example.ui.theme.OmigramBorder
import com.example.ui.theme.OmigramPrimaryText
import com.example.ui.theme.OmigramSecondaryBackground
import com.example.ui.theme.OmigramSecondaryText
import com.example.ui.theme.SocialBrandBlue
import com.example.ui.theme.SocialSoftIconBg

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

    var viewingStoryIndex by remember { mutableStateOf<Int?>(null) }
    var activePostForComments by remember { mutableStateOf<Post?>(null) }
    var isCreatePostOpen by remember { mutableStateOf(false) }

    val currentPillTab = when (uiState.selectedTab) {
        OmigramTab.HOME -> SocialNavTab.HOME
        OmigramTab.REELS -> SocialNavTab.REELS
        OmigramTab.PROFILE -> SocialNavTab.FRIENDS
        else -> SocialNavTab.HOME
    }
    val unread = chats.sumOf { it.unreadCount }

    Scaffold(
        modifier = modifier.fillMaxSize().background(OmigramBackground).testTag("home_screen"),
        containerColor = OmigramBackground,
        topBar = {
            if (uiState.selectedTab == OmigramTab.HOME) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OmigramBackground)
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(SocialBrandBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("O", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Black)
                        }
                        Spacer(Modifier.width(9.dp))
                        Text(
                            text = "Omigram",
                            color = OmigramPrimaryText,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.7).sp,
                            modifier = Modifier.testTag("home_brand_logo")
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HomeHeaderButton(
                            icon = Icons.Outlined.Search,
                            description = "Search",
                            tag = "top_bar_search_button",
                            onClick = { viewModel.selectTab(OmigramTab.SEARCH) }
                        )
                        Surface(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .clickable(onClick = onNavigateToMessages)
                                .testTag("top_bar_messages_button"),
                            color = SocialSoftIconBg,
                            shape = CircleShape
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                BadgedBox(
                                    badge = {
                                        if (unread > 0) {
                                            Badge(containerColor = SocialBrandBlue) { Text(unread.coerceAtMost(99).toString()) }
                                        }
                                    }
                                ) {
                                    DmProIcon(tint = OmigramPrimaryText, modifier = Modifier.size(21.dp))
                                }
                            }
                        }
                        Surface(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .border(1.dp, OmigramBorder, CircleShape)
                                .clickable { viewModel.selectTab(OmigramTab.PROFILE) }
                                .testTag("top_bar_profile_avatar_button"),
                            color = SocialSoftIconBg,
                            shape = CircleShape
                        ) {
                            AsyncImage(
                                model = currentUser?.avatarUrl,
                                contentDescription = "My profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
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
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding).background(OmigramBackground)
        ) {
            when (uiState.selectedTab) {
                OmigramTab.HOME -> FeedTabContent(
                    stories = stories,
                    posts = posts,
                    onStoryClick = { viewingStoryIndex = it },
                    onLikeToggle = viewModel::toggleLikePost,
                    onCommentClick = { activePostForComments = it },
                    onShareClick = { onNavigateToMessages() },
                    onSaveToggle = viewModel::toggleSavePost,
                    onUserClick = { onNavigateToUserProfile(it) }
                )
                OmigramTab.SEARCH -> ExploreScreen(onNavigateToUserProfile = onNavigateToUserProfile)
                OmigramTab.CREATE -> CreateScreen(
                    onDismiss = { viewModel.selectTab(OmigramTab.HOME) },
                    onPostCreated = { viewModel.addNewPost(it) },
                    onStoryCreated = { viewModel.addNewStory(it) },
                    onReelCreated = { viewModel.addNewReel(it) }
                )
                OmigramTab.REELS -> ReelsScreen(onNavigateToUserProfile = onNavigateToUserProfile)
                OmigramTab.PROFILE -> ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToSettings = onNavigateToSettings,
                    showBackButton = false
                )
            }
        }
    }

    if (isCreatePostOpen) {
        CreateScreen(
            onDismiss = { isCreatePostOpen = false },
            onPostCreated = { viewModel.addNewPost(it); isCreatePostOpen = false },
            onStoryCreated = { viewModel.addNewStory(it); isCreatePostOpen = false },
            onReelCreated = { viewModel.addNewReel(it); isCreatePostOpen = false }
        )
    }
    viewingStoryIndex?.let { index ->
        StoryViewerDialog(stories = stories, initialStoryIndex = index, onDismiss = { viewingStoryIndex = null })
    }
    activePostForComments?.let { post ->
        CommentsSheet(post = post, onDismiss = { activePostForComments = null })
    }
}

@Composable
private fun HomeHeaderButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.size(42.dp).clip(CircleShape).clickable(onClick = onClick).testTag(tag),
        color = SocialSoftIconBg,
        shape = CircleShape
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = description, tint = OmigramPrimaryText, modifier = Modifier.size(21.dp))
        }
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
        modifier = Modifier.fillMaxSize().testTag("feed_list"),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item { StoriesTray(stories, onStoryClick) }
        items(posts, key = { it.id }) { post ->
            PostItemCard(
                post = post,
                onLikeToggle = { onLikeToggle(post.id) },
                onCommentClick = { onCommentClick(post) },
                onShareClick = { onShareClick(post) },
                onSaveToggle = { onSaveToggle(post.id) },
                onUserClick = { onUserClick(post.author.id) }
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StoriesTray(stories: List<Story>, onStoryClick: (Int) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).testTag("stories_tray"),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(
                modifier = Modifier.width(104.dp).height(160.dp).clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEDEEFF)).clickable { if (stories.isNotEmpty()) onStoryClick(0) }
                    .testTag("add_story_card"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(SocialBrandBlue),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Add, "Add story", tint = Color.White) }
                Spacer(Modifier.height(9.dp))
                Text("Add story", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SocialBrandBlue)
            }
        }
        itemsIndexed(stories) { index, story ->
            Box(
                modifier = Modifier.width(104.dp).height(160.dp).clip(RoundedCornerShape(18.dp))
                    .background(OmigramSecondaryBackground).clickable { onStoryClick(index) }
                    .testTag("story_card_${story.id}")
            ) {
                AsyncImage(
                    model = story.mediaUrl,
                    contentDescription = story.user.fullName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier.fillMaxWidth().height(82.dp).align(Alignment.BottomCenter)
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = .8f))))
                )
                Row(
                    modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = story.user.avatarUrl,
                        contentDescription = story.user.fullName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(26.dp).clip(CircleShape).border(1.5.dp, Color.White, CircleShape)
                    )
                    Spacer(Modifier.width(6.dp))
                    Column {
                        Text(story.user.fullName.ifEmpty { story.user.username }, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(story.timeAgo, color = Color.White.copy(alpha = .82f), fontSize = 9.sp, maxLines = 1)
                    }
                }
            }
        }
    }
}
