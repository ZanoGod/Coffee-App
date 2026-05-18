@file:Suppress("DEPRECATION")

package com.zano.mistcafe.ui.theme

import android.R
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    onBackground = DarkOnSurface,

)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightSecondary,
    background = LightBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    onBackground = Color.Black
)

@Composable
fun MistCafeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            if (darkTheme) {
                // === DARK MODE STATUS BAR ===
                // Usually matches the dark background
                window.statusBarColor = MistNavy.toArgb()

                // Set to 'false' for WHITE icons (Battery, Time, etc.)
                insetsController.isAppearanceLightStatusBars = false
            } else {
                // === LIGHT MODE STATUS BAR ===

                // OPTION 1: Seamless with Header (Recommended for your App)
                // Since your TopBars are MistNavy, this blends them perfectly.
                window.statusBarColor = MistNavy.toArgb()
                insetsController.isAppearanceLightStatusBars = false // White icons on Navy

                // OPTION 2: Standard Android (Matches Background)
                // If you want the status bar to be Beige/Cream like the background:
                // window.statusBarColor = MistCream.toArgb()
                // insetsController.isAppearanceLightStatusBars = true // Dark icons on Cream
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Ensure you have Typography defined
        content = content
    )
}