package com.snifferdog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val DarkScheme = darkColorScheme(
    primary = SnifferColors.Primary,
    onPrimary = SnifferColors.Background,
    secondary = SnifferColors.Accent,
    background = SnifferColors.Background,
    surface = SnifferColors.Background,
    onBackground = SnifferColors.Text,
    onSurface = SnifferColors.Text,
    onSurfaceVariant = SnifferColors.Muted,
    outline = SnifferColors.Border,
)

val LocalSnifferColors = staticCompositionLocalOf { SnifferColors }

@Composable
fun SnifferDogTheme(
    // Product is dark-first per PRD; ignore system light for now.
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val scheme = if (darkTheme || !isSystemInDarkTheme()) DarkScheme else DarkScheme
    CompositionLocalProvider(LocalSnifferColors provides SnifferColors) {
        MaterialTheme(
            colorScheme = scheme,
            typography = SnifferTypography,
            content = content,
        )
    }
}
