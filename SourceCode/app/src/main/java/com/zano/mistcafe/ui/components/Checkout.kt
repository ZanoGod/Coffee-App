package com.zano.mistcafe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zano.mistcafe.ui.theme.MistCream
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts

enum class OrderType {
    DELIVERY, PICKUP
}

enum class PaymentMethod {
    CASH, CARD
}

// --- ORDER TYPE SEGMENTED CONTROL ---
@Composable
fun SegmentedOrderType(
    selected: OrderType,
    onSelected: (OrderType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            // Use surfaceContainerHighest or MistCream for the track
            // In Dark Mode, we want the track to be slightly lighter than background or dark
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SegmentedButton(
            text = "Delivery",
            selected = selected == OrderType.DELIVERY,
            modifier = Modifier.weight(1f)
        ) { onSelected(OrderType.DELIVERY) }

        SegmentedButton(
            text = "Pickup",
            selected = selected == OrderType.PICKUP,
            modifier = Modifier.weight(1f)
        ) { onSelected(OrderType.PICKUP) }
    }
}

@Composable
fun SegmentedButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    // Background: Navy (Selected) vs Transparent (Unselected)
    val containerColor by animateColorAsState(
        targetValue = if (selected) MistNavy else Color.Transparent,
        animationSpec = tween(300),
        label = "bgColor"
    )

    // Text: White (Selected) vs Navy (Unselected)
    // [FIX] Changed unselected from Gray to MistNavy/OnSurface for better contrast in Light Mode
    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(300),
        label = "textColor"
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontFamily = myFonts,
            fontSize = 15.sp
        )
    }
}

// --- PAYMENT OPTION CARD ---
@Composable
fun PaymentOption(
    title: String,
    img: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    // [FIX] Background: Use 'surface' color (White in Light, MistBlue in Dark)
    // If selected, we apply a subtle tint of the primary color
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    // Border: Primary if selected, Outline if not
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Background
            Box(
                modifier = Modifier
                    .size(42.dp)
                    // [FIX] Use surfaceVariant for icon background (works in both modes)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = img),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // [FIX] Text Color: Use onSurface so it is White in Dark Mode
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontFamily = myFonts,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 16.sp,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )

            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}