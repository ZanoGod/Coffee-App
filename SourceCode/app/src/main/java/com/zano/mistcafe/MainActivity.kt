package com.zano.mistcafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zano.mistcafe.ui.theme.MistCafeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            // 1. Create the State for Theme
            // Default to system setting (Auto)
            val systemDark = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemDark) }

            // 2. Wrap the app in your Theme
            // Pass the state variable to the theme
            MistCafeTheme(darkTheme = isDarkTheme) {

                // 3. Pass state & setter to Navigator
                Navigator(
                    isDarkTheme = isDarkTheme,
                    onThemeChanged = { newMode -> isDarkTheme = newMode }
                )
            }
        }
    }
}