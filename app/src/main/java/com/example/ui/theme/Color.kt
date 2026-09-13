package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Instagram and Modern Social Palette
val OmigramBackground = Color(0xFFFFFFFF)        // Pure White
val OmigramSecondaryBackground = Color(0xFFF8F9FA)
val OmigramPrimaryText = Color(0xFF111111)
val OmigramSecondaryText = Color(0xFF71767B)
val OmigramBorder = Color(0xFFE5E7EB)
val OmigramAccentBlue = Color(0xFF1877F2)        // Modern Vibrant Blue
val OmigramErrorRed = Color(0xFFED4956)
val OmigramSuccessGreen = Color(0xFF34C759)

// Screenshot Signature Colors
val SocialBrandBlue = Color(0xFF1877F2)
val SocialPillDark = Color(0xFF1C1C1E)
val SocialPillLight = Color(0xFFF2F4F7)
val SocialPeachGradientTop = Color(0xFFFBECE5)
val SocialPeachGradientMid = Color(0xFFF8F1ED)
val SocialPeachGradientBottom = Color(0xFFFFFFFF)
val SocialAvatarRingCoral = Color(0xFFE8503A)
val SocialSoftCardBg = Color(0xFFF5F6F8)
val SocialSoftIconBg = Color(0xFFF0F2F5)
val SocialToggleGreen = Color(0xFF34C759)

// Chat Bubbles
val SentBubbleBlue = Color(0xFF0095F6)
val ReceivedBubbleGray = Color(0xFFEFEFEF)
val SentBubbleText = Color(0xFFFFFFFF)
val ReceivedBubbleText = Color(0xFF000000)

// Online presence & Badges
val OnlineGreen = Color(0xFF2ECC71)
val UnreadBadgeBlue = Color(0xFF0095F6)
val VerifiedBadgeBlue = Color(0xFF0095F6)
val MissedCallRed = Color(0xFFED4956)

// Gradient for Story Ring (Instagram Style)
val InstagramGradient = listOf(
    Color(0xFFFBAA47),
    Color(0xFFD91A46),
    Color(0xFFA60F93)
)

val StoryGradient = Brush.linearGradient(colors = InstagramGradient)

// Legacy aliases for components
val OmigoPrimaryLight = OmigramAccentBlue
val OmigoOnPrimaryLight = Color(0xFFFFFFFF)
val OmigoPrimaryContainerLight = Color(0xFFE0F2FE)
val OmigoOnPrimaryContainerLight = Color(0xFF03354E)

val OmigoSecondaryLight = OmigramSecondaryText
val OmigoOnSecondaryLight = Color(0xFFFFFFFF)
val OmigoSecondaryContainerLight = OmigramSecondaryBackground
val OmigoOnSecondaryContainerLight = OmigramPrimaryText

val OmigoTertiaryLight = OmigramAccentBlue
val OmigoOnTertiaryLight = Color(0xFFFFFFFF)
val OmigoTertiaryContainerLight = Color(0xFFE0F2FE)
val OmigoOnTertiaryContainerLight = Color(0xFF03354E)

val OmigoBackgroundLight = OmigramBackground
val OmigoOnBackgroundLight = OmigramPrimaryText
val OmigoSurfaceLight = OmigramBackground
val OmigoOnSurfaceLight = OmigramPrimaryText
val OmigoSurfaceVariantLight = OmigramSecondaryBackground
val OmigoOnSurfaceVariantLight = OmigramSecondaryText
val OmigoOutlineLight = OmigramBorder

val SentBubbleLight = SentBubbleBlue
val ReceivedBubbleLight = ReceivedBubbleGray
val ReadReceiptBlue = OmigramAccentBlue
