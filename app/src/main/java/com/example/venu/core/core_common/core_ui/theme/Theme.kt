package com.example.venu.core.core_common.core_ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext



private val DarkColorScheme = darkColorScheme(

    // PRIMARY
    primary = VenuColors.AccentBlue,
    onPrimary = Color.White,

    primaryContainer = Color(0xFF1E3A5F),
    onPrimaryContainer = Color(0xFFEAF2FF),

    // SECONDARY
    secondary = Color(0xFF3A2618),
    onSecondary = Color(0xFFFFB86B),

    // TERTIARY
    tertiary = Color(0xFF25304A),
    onTertiary = Color(0xFFEAEFFD),

    // BACKGROUNDS
    background = Color(0xFF101318),
    onBackground = Color(0xFFF4F5F7),

    surface = Color(0xFF171A21),
    onSurface = Color(0xFFF4F5F7),

    surfaceVariant = Color(0xFF222631),
    onSurfaceVariant = Color(0xFFC6CBD4),

    // BORDERS / DIVIDERS
    outline = Color(0xFF343946),
    outlineVariant = Color(0xFF4B5263),

    // EXTRA
    inverseSurface = Color(0xFFF4F5F7),
    inverseOnSurface = Color(0xFF101318)
)

private val LightColorScheme = lightColorScheme(

    // PRIMARY (main actions: buttons, highlights)
    primary = VenuColors.AccentBlue,
    onPrimary = Color.White,

    primaryContainer = VenuColors.AccentBlueBorder,
    onPrimaryContainer = VenuColors.TextPrimary,

    // SECONDARY (chips / softer accents)
    secondary = VenuColors.GenreChipBg,
    onSecondary = VenuColors.GenreChipText,

    // TERTIARY (optional accent — keep subtle)
    tertiary = VenuColors.AvatarBg,
    onTertiary = VenuColors.TextPrimary,

    // BACKGROUNDS
    background = VenuColors.Background,
    onBackground = VenuColors.TextPrimary,

    surface = VenuColors.Background,
    onSurface = VenuColors.TextPrimary,

    surfaceVariant = VenuColors.SurfaceMuted,
    onSurfaceVariant = VenuColors.TextSecondary,

    // BORDERS / DIVIDERS
    outline = VenuColors.Border,
    outlineVariant = VenuColors.BorderDark,

    // EXTRA (nice to have)
    inverseSurface = VenuColors.TextPrimary,
    inverseOnSurface = VenuColors.Background

)

@Composable
fun VenuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}