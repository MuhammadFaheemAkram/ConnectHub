package com.example.connecthub.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = AppSurface,
    secondary = BlueSecondary,
    tertiary = AmberTertiary,
    background = AppBackground,
    onBackground = TextStrong,
    surface = AppSurface,
    onSurface = TextStrong,
)

private val DarkColorScheme = darkColorScheme(
    primary = TealPrimaryDark,
    onPrimary = AppBackgroundDark,
    secondary = BlueSecondaryDark,
    tertiary = AmberTertiaryDark,
    background = AppBackgroundDark,
    onBackground = TextStrongDark,
    surface = AppSurfaceDark,
    onSurface = TextStrongDark,
)

@Composable
fun ConnectHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
        typography = ConnectHubTypography,
        content = content,
    )
}
