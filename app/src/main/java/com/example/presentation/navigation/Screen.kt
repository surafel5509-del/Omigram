package com.example.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Notifications : Screen("notifications")
    object Messages : Screen("messages")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")

    object ChatDetail : Screen("chat_detail/{chatId}") {
        fun createRoute(chatId: String): String = "chat_detail/$chatId"
    }

    object UserProfile : Screen("user_profile/{userId}") {
        fun createRoute(userId: String): String = "user_profile/$userId"
    }

    object Call : Screen("call/{userId}/{isVideo}") {
        fun createRoute(userId: String, isVideo: Boolean): String = "call/$userId/$isVideo"
    }
}
