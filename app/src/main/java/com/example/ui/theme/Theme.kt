package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OmigramDarkColorScheme = darkColorScheme(
    primary = OmigramAccentBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color(0xFFF1F5F9),
    secondary = Color(0xFF94A3B8),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E293B),
    onSecondaryContainer = Color(0xFFF1F5F9),
    tertiary = OmigramAccentBlue,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF1E293B),
    onTertiaryContainer = Color(0xFFF1F5F9),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B),
    error = OmigramErrorRed,
    onError = Color.White
)

// Force Light Mode Only Palette as specified
private val OmigramLightColorScheme = lightColorScheme(
    primary = OmigramAccentBlue,
    onPrimary = OmigramBackground,
    primaryContainer = OmigramSecondaryBackground,
    onPrimaryContainer = OmigramPrimaryText,
    secondary = OmigramSecondaryText,
    onSecondary = OmigramBackground,
    secondaryContainer = OmigramSecondaryBackground,
    onSecondaryContainer = OmigramPrimaryText,
    tertiary = OmigramAccentBlue,
    onTertiary = OmigramBackground,
    tertiaryContainer = OmigramSecondaryBackground,
    onTertiaryContainer = OmigramPrimaryText,
    background = OmigramBackground,
    onBackground = OmigramPrimaryText,
    surface = OmigramBackground,
    onSurface = OmigramPrimaryText,
    surfaceVariant = OmigramSecondaryBackground,
    onSurfaceVariant = OmigramSecondaryText,
    outline = OmigramBorder,
    outlineVariant = OmigramBorder.copy(alpha = 0.5f),
    error = OmigramErrorRed,
    onError = OmigramBackground
)

@Composable
fun OmigramTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) OmigramDarkColorScheme else OmigramLightColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward-compatible aliases
@Composable
fun OmigoChatTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    OmigramTheme(content = content)
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    OmigramTheme(content = content)
}
