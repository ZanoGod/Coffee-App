package com.zano.mistcafe.ui.theme

import androidx.compose.ui.graphics.Color

// --- YOUR BRAND COLORS ---
val MistNavy = Color(0xFF213555)       // myColor1
val MistBlue = Color(0xFF3E5879)       // myColor2
val MistBeige = Color(0xFFD8C4B6)      // bgColor1
val MistCream = Color(0xFFF5EFE7)      // bgColor2

// --- LIGHT THEME PALETTE ---
val LightPrimary = MistNavy
val LightOnPrimary = Color.White
val LightSecondary = MistBlue
val LightBackground = MistCream
val LightSurface = Color.White
val LightOnSurface = Color.Black

// --- DARK THEME PALETTE ---
// In dark mode, we use the Navy as background and Beige/Cream for text/elements
val DarkPrimary = MistBeige            // Beige pops well on Navy
val DarkOnPrimary = MistNavy
val DarkSecondary = MistCream
val DarkBackground = MistNavy          // Dark Navy Background
val DarkSurface = MistBlue             // Lighter Navy for Cards
val DarkOnSurface = MistCream          // Cream text on Navy