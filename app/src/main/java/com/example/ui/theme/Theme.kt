package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OmigramDarkColorScheme = darkColorScheme(
    primary = OmigramOrange,
    onPrimary = Color.White,
    primaryContainer = OmigramDarkSurfaceVariant,
    onPrimaryContainer = Color.White,
    secondary = OmigramSecondaryText,
    onSecondary = Color.White,
    secondaryContainer = OmigramDarkSurfaceVariant,
    onSecondaryContainer = Color.White,
    tertiary = OmigramOrangeGlow,
    onTertiary = Color.White,
    tertiaryContainer = OmigramDarkSurfaceVariant,
    onTertiaryContainer = Color.White,
    background = OmigramDarkBg,
    onBackground = Color(0xFFF8FAFC),
    surface = OmigramDarkSurface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = OmigramDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF2E2E38),
    outlineVariant = Color(0xFF1E1E28),
    error = OmigramErrorRed,
    onError = Color.White
)

private val OmigramLightColorScheme = lightColorScheme(
    primary = OmigramOrange,
    onPrimary = Color.White,
    primaryContainer = OmigramOrangeSoft,
    onPrimaryContainer = OmigramOrangeDark,
    secondary = OmigramSecondaryText,
    onSecondary = Color.White,
    secondaryContainer = OmigramSecondaryBackground,
    onSecondaryContainer = OmigramPrimaryText,
    tertiary = OmigramOrangeGlow,
    onTertiary = Color.White,
    tertiaryContainer = OmigramSecondaryBackground,
    onTertiaryContainer = OmigramPrimaryText,
    background = OmigramBackground,
    onBackground = OmigramPrimaryText,
    surface = Color.White,
    onSurface = OmigramPrimaryText,
    surfaceVariant = OmigramSecondaryBackground,
    onSurfaceVariant = OmigramSecondaryText,
    outline = OmigramBorder,
    outlineVariant = OmigramBorder.copy(alpha = 0.5f),
    error = OmigramErrorRed,
    onError = Color.White
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
    OmigramTheme(darkTheme = darkTheme, content = content)
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    OmigramTheme(darkTheme = darkTheme, content = content)
}

// Theme helper accessors for dynamic dark/light mode adaptation
val appSurface: Color
    @Composable
    get() = MaterialTheme.colorScheme.surface

val appBackground: Color
    @Composable
    get() = MaterialTheme.colorScheme.background

val appTextPrimary: Color
    @Composable
    get() = MaterialTheme.colorScheme.onBackground

val appTextSecondary: Color
    @Composable
    get() = MaterialTheme.colorScheme.onSurfaceVariant

val appBorder: Color
    @Composable
    get() = MaterialTheme.colorScheme.outline

val appSurfaceVariant: Color
    @Composable
    get() = MaterialTheme.colorScheme.surfaceVariant

