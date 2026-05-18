package com.zano.mistcafe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts
import kotlinx.coroutines.delay

@Composable
fun CustomGlassSnackbar(data: SnackbarData) {
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            // MistNavy with Glass Opacity
            containerColor = MistNavy.copy(alpha = 0.90f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.2f)
        ),
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.85f) // Takes up 85% of screen width
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 24.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = data.visuals.message,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = myFonts,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AnimatedSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val currentData = hostState.currentSnackbarData

    // 1. Remember the last data so it doesn't disappear instantly when dismissed
    var activeData by remember { mutableStateOf<SnackbarData?>(null) }

    // Update activeData only when a new snackbar appears
    if (currentData != null) {
        activeData = currentData
    }

    // 2. Custom Duration Logic (1.5 seconds)
    LaunchedEffect(currentData) {
        if (currentData != null) {
            delay(1500) // Wait 1.5 seconds
            currentData.dismiss() // Manually dismiss to trigger exit animation
        }
    }

    // 3. Animation Logic
    AnimatedVisibility(
        visible = currentData != null,
        // Enter: Slide down from top (-100%) + Fade In
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(tween(durationMillis = 300)),

        // Exit: Slide up to top (-100%) + Fade Out
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeOut(tween(durationMillis = 300)),

        modifier = modifier
    ) {
        // Render the activeData (which persists during the exit animation)
        activeData?.let { data ->
            CustomGlassSnackbar(data = data)
        }
    }
}