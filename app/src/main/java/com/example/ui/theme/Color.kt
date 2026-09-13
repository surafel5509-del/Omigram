package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Omigram visual system: inspired by the supplied soft social references, but intentionally original. */
val OmigramBackground = Color(0xFFFFFCFA)
val OmigramSecondaryBackground = Color(0xFFF6F7F9)
val OmigramPrimaryText = Color(0xFF17171A)
val OmigramSecondaryText = Color(0xFF74747C)
val OmigramBorder = Color(0xFFE6E7EB)
val OmigramAccentBlue = Color(0xFF5B5FEF)
val OmigramBrandIndigo = Color(0xFF5B5FEF)
val OmigramBrandCoral = Color(0xFFFF6B5F)
val OmigramErrorRed = Color(0xFFE5484D)
val OmigramSuccessGreen = Color(0xFF22C55E)

val SocialBrandBlue = OmigramBrandIndigo
val SocialPillDark = Color(0xFF19191D)
val SocialPillLight = Color(0xFFF1F2F5)
val SocialPeachGradientTop = Color(0xFFFFEEE7)
val SocialPeachGradientMid = Color(0xFFFFF5F0)
val SocialPeachGradientBottom = OmigramBackground
val SocialAvatarRingCoral = OmigramBrandCoral
val SocialSoftCardBg = Color(0xFFF7F7F9)
val SocialSoftIconBg = Color(0xFFF1F2F5)
val SocialToggleGreen = OmigramSuccessGreen

val SentBubbleBlue = OmigramBrandIndigo
val ReceivedBubbleGray = Color(0xFFF0F0F2)
val SentBubbleText = Color.White
val ReceivedBubbleText = OmigramPrimaryText

val OnlineGreen = OmigramSuccessGreen
val UnreadBadgeBlue = OmigramBrandIndigo
val VerifiedBadgeBlue = OmigramBrandIndigo
val MissedCallRed = OmigramErrorRed

val InstagramGradient = listOf(OmigramBrandCoral, Color(0xFFFFA45B), OmigramBrandIndigo)
val StoryGradient = Brush.linearGradient(colors = InstagramGradient)

// Legacy aliases retained for existing screens.
val OmigoPrimaryLight = OmigramAccentBlue
val OmigoOnPrimaryLight = Color.White
val OmigoPrimaryContainerLight = Color(0xFFE9E9FF)
val OmigoOnPrimaryContainerLight = Color(0xFF25255C)
val OmigoSecondaryLight = OmigramSecondaryText
val OmigoOnSecondaryLight = Color.White
val OmigoSecondaryContainerLight = OmigramSecondaryBackground
val OmigoOnSecondaryContainerLight = OmigramPrimaryText
val OmigoTertiaryLight = OmigramBrandCoral
val OmigoOnTertiaryLight = Color.White
val OmigoTertiaryContainerLight = Color(0xFFFFE5E1)
val OmigoOnTertiaryContainerLight = Color(0xFF5A1B14)
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
