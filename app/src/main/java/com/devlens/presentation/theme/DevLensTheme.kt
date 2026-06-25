package com.devlens.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1D4ED8),
    onPrimary = Color.White,
    secondary = Color(0xFF047857),
    tertiary = Color(0xFFB45309),
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    surfaceVariant = Color(0xFFE2E8F0),
    error = Color(0xFFB91C1C)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF93C5FD),
    secondary = Color(0xFF6EE7B7),
    tertiary = Color(0xFFFCD34D),
    background = Color(0xFF0F172A),
    surface = Color(0xFF111827),
    surfaceVariant = Color(0xFF1F2937),
    error = Color(0xFFFCA5A5)
)

@Composable
fun DevLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = DevLensTypography,
        content = content
    )
}
