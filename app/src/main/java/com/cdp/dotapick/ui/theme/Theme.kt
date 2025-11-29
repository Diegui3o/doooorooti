package com.cdp.dotapick.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DotaDarkColorScheme = darkColorScheme(
    primary = DotaRed,
    onPrimary = Color.White,
    primaryContainer = DotaCardLight,
    onPrimaryContainer = DotaTextPrimary,

    secondary = DotaBlue,
    onSecondary = Color.White,
    secondaryContainer = DotaCardLight,
    onSecondaryContainer = DotaTextPrimary,

    tertiary = DotaGreen,
    onTertiary = Color.White,
    tertiaryContainer = DotaCardLight,
    onTertiaryContainer = DotaTextPrimary,

    background = DotaDarkGray,
    onBackground = DotaTextPrimary,

    surface = DotaCardDark,
    onSurface = DotaTextPrimary,

    surfaceVariant = DotaCardLight,
    onSurfaceVariant = DotaTextSecondary,

    outline = Color(0xFF404040)
)

private val DotaLightColorScheme = lightColorScheme(
    primary = DotaRed,
    secondary = DotaBlue,
    tertiary = DotaGreen
)

@Composable
fun DotaPickTheme(
    darkTheme: Boolean = true, // Siempre tema oscuro
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DotaDarkColorScheme
    } else {
        DotaLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}