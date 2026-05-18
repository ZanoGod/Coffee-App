package com.zano.mistcafe.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zano.mistcafe.ui.theme.MistBeige
import com.zano.mistcafe.ui.theme.MistBlue
import com.zano.mistcafe.ui.theme.MistCream
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts

@Composable
fun BouncingDotsLoadingView(
    message: String = "Please Wait...",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val dotSize = 12.dp
    val delayUnit = 300 // ms

    @Composable
    fun Dot(scale: Float) {
        Box(
            Modifier
                .size(dotSize)
                .scale(scale)
                .background(
                    color = MistNavy, // Brand Navy
                    shape = CircleShape
                )
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    @Composable
    fun animateScaleWithDelay(delay: Int): State<Float> {
        return infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = delayUnit * 4
                    0f at 0
                    0f at delay using LinearEasing
                    1f at delay + delayUnit using FastOutSlowInEasing
                    0f at delay + delayUnit * 2
                }
            ), label = "dot-scale"
        )
    }

    val scale1 by animateScaleWithDelay(0)
    val scale2 by animateScaleWithDelay(delayUnit)
    val scale3 by animateScaleWithDelay(delayUnit * 2)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // MistCream in Light, Navy in Dark
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Dot(scale1)
                Dot(scale2)
                Dot(scale3)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = message,
                fontFamily = myFonts,
                color = MistBlue, // Lighter Blue for text
                fontSize = 20.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MistNavy,
            trackColor = MistBeige,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun CustomBrandLoadingView(
    message: String = "LOADING",
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "spin")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "angle"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(80.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 8.dp.toPx()

                    // 1. Static Track (Beige)
                    drawCircle(
                        color = MistBeige,
                        style = Stroke(width = strokeWidth)
                    )

                    // 2. Rotating Indicator (Navy)
                    drawArc(
                        color = MistNavy,
                        startAngle = angle,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = message.uppercase(),
                fontFamily = myFonts,
                color = MistNavy,
                fontSize = 22.sp,
                letterSpacing = 2.sp
            )
        }
    }
}