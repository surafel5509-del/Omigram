package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Omigram visual system: inspired by the supplied soft social references, but intentionally original. */
val OmigramBackground = Color(0xFFFAF9F7)
val OmigramSecondaryBackground = Color(0xFFF4F5F8)
val OmigramPrimaryText = Color(0xFF17171A)
val OmigramSecondaryText = Color(0xFF8E8E93)
val OmigramBorder = Color(0xFFEBECEF)
val OmigramAccentBlue = Color(0xFF2563EB)
val OmigramBrandIndigo = Color(0xFF2563EB)
val OmigramBrandCoral = Color(0xFFC85E43)
val OmigramErrorRed = Color(0xFFE5484D)
val OmigramSuccessGreen = Color(0xFF34C759)

val SocialBrandBlue = Color(0xFF2563EB)
val SocialPillDark = Color(0xFF19191D)
val SocialPillLight = Color(0xFFF1F2F5)
val SocialPeachGradientTop = Color(0xFFFBECE4)
val SocialPeachGradientMid = Color(0xFFFBF4EF)
val SocialPeachGradientBottom = Color(0xFFFAF6F2)
val SocialAvatarRingCoral = Color(0xFFC85E43)
val SocialSoftCardBg = Color(0xFFF5F6F8)
val SocialSoftIconBg = Color(0xFFF3F4F6)
val SocialToggleGreen = Color(0xFF34C759)
val SocialVerifiedBadgeRed = Color(0xFFC85E43)

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
