package com.zano.mistcafe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zano.mistcafe.R
import com.zano.mistcafe.ui.theme.MistBlue
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts

@Composable
fun CoffeeHeader(username: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
    ) {
        // 1. Background Image
        Image(
            painter = painterResource(id = R.drawable.splash_screen),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Dark Gradient Overlay (Mist Theme)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MistNavy.copy(alpha = 0.85f), // Top: Dark Navy
                            MistBlue.copy(alpha = 0.95f)  // Bottom: Lighter Blue
                        )
                    )
                )
        )

        // 3. Content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Picture
            Image(
                painter = painterResource(id = R.drawable.person),
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Welcome Text
            Column {
                Text(
                    text = "Welcome back,",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontFamily = myFonts,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = username,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontFamily = myFonts,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}