package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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
    content: @Composable () -> Unit
) {
    // Light mode forced: ignore system dark theme and dynamic theming
    MaterialTheme(
        colorScheme = OmigramLightColorScheme,
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
