package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Omigram visual system: sleek modern social styling with warm orange accents and dark elegance. */
val OmigramOrange = Color(0xFFFF6A00)
val OmigramOrangeGlow = Color(0xFFFF8533)
val OmigramOrangeSoft = Color(0xFFFFF0E6)
val OmigramOrangeDark = Color(0xFFE65100)

val OmigramDarkBg = Color(0xFF0D0D11)
val OmigramDarkSurface = Color(0xFF16161D)
val OmigramDarkSurfaceVariant = Color(0xFF22222B)

val OmigramBackground = Color(0xFFFAF9F7)
val OmigramSecondaryBackground = Color(0xFFF4F5F8)
val OmigramPrimaryText = Color(0xFF17171A)
val OmigramSecondaryText = Color(0xFF8E8E93)
val OmigramBorder = Color(0xFFEBECEF)
val OmigramAccentBlue = OmigramOrange
val OmigramBrandIndigo = OmigramOrange
val OmigramBrandCoral = OmigramOrange
val OmigramErrorRed = Color(0xFFE5484D)
val OmigramSuccessGreen = Color(0xFF34C759)

val SocialBrandBlue = OmigramOrange
val SocialPillDark = Color(0xFF19191D)
val SocialPillLight = Color(0xFFF1F2F5)
val SocialPeachGradientTop = Color(0xFFFDECE2)
val SocialPeachGradientMid = Color(0xFFFDF4EE)
val SocialPeachGradientBottom = Color(0xFFFAF6F2)
val SocialAvatarRingCoral = OmigramOrange
val SocialSoftCardBg = Color(0xFFF5F6F8)
val SocialSoftIconBg = Color(0xFFF3F4F6)
val SocialToggleGreen = Color(0xFF34C759)
val SocialVerifiedBadgeRed = OmigramOrange

val SentBubbleBlue = OmigramOrange
val ReceivedBubbleGray = Color(0xFFF0F0F2)
val SentBubbleText = Color.White
val ReceivedBubbleText = OmigramPrimaryText

val OnlineGreen = OmigramSuccessGreen
val UnreadBadgeBlue = OmigramOrange
val VerifiedBadgeBlue = OmigramOrange
val MissedCallRed = OmigramErrorRed

val OmigramGradient = listOf(OmigramOrange, Color(0xFFFFA040), Color(0xFFFF5722))
val StoryGradient = Brush.linearGradient(colors = OmigramGradient)

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
