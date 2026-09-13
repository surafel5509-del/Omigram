package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.di.AppContainer
import com.example.presentation.auth.AuthViewModel
import com.example.presentation.auth.ForgotPasswordScreen
import com.example.presentation.auth.LoginScreen
import com.example.presentation.auth.RegisterScreen
import com.example.presentation.chat.ChatScreen
import com.example.presentation.chat.ChatViewModel
import com.example.presentation.home.HomeScreen
import com.example.presentation.home.HomeViewModel
import com.example.presentation.messages.MessagesScreen
import com.example.presentation.notifications.NotificationsScreen
import com.example.presentation.onboarding.OnboardingScreen
import com.example.presentation.profile.ProfileScreen
import com.example.presentation.profile.ProfileViewModel
import com.example.presentation.profile.UserProfileScreen
import com.example.presentation.settings.AppThemeMode
import com.example.presentation.settings.SettingsScreen
import com.example.presentation.splash.SplashScreen

@Composable
fun OmigoNavGraph(
    navController: NavHostController,
    container: AppContainer,
    currentThemeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                authRepository = container.authRepository,
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModel.Factory(container.authRepository)
            )
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.Register.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModel.Factory(container.authRepository)
            )
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModel.Factory(container.authRepository)
            )
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.Factory(
                    container.authRepository,
                    container.userRepository,
                    container.chatRepository,
                    container.contactRepository,
                    container.callRepository
                )
            )
            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.Factory(
                    container.authRepository,
                    container.userRepository
                )
            )
            HomeScreen(
                viewModel = homeViewModel,
                profileViewModel = profileViewModel,
                onNavigateToChat = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                },
                onNavigateToNotifications = {
                    navController.navigate(Screen.Notifications.route)
                },
                onNavigateToMessages = {
                    navController.navigate(Screen.Messages.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(Screen.UserProfile.createRoute(userId))
                }
            )
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(Screen.UserProfile.createRoute(userId))
                }
            )
        }

        composable(Screen.Messages.route) {
            MessagesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToChat = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                }
            )
        }

        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            val chatViewModel: ChatViewModel = viewModel(
                factory = ChatViewModel.Factory(
                    chatId = chatId,
                    chatRepository = container.chatRepository,
                    messageRepository = container.messageRepository,
                    userRepository = container.userRepository
                )
            )
            ChatScreen(
                viewModel = chatViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.Factory(
                    container.authRepository,
                    container.userRepository
                )
            )
            ProfileScreen(
                viewModel = profileViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                showBackButton = true
            )
        }

        composable(
            route = Screen.UserProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserProfileScreen(
                userId = userId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToChat = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                }
            )
        }

        composable(Screen.Settings.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModel.Factory(container.authRepository)
            )
            SettingsScreen(
                currentThemeMode = currentThemeMode,
                onThemeModeChange = onThemeModeChange,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
