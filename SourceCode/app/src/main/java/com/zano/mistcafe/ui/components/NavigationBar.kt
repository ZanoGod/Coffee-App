package com.zano.mistcafe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.zano.mistcafe.Route
import com.zano.mistcafe.ui.theme.MistNavy

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    data object Cart : BottomNavItem("cart", Icons.Default.ShoppingCart, "Cart")
    data object Favorites : BottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
    data object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    userId: Int = 0
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 24.dp, end = 24.dp)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            shape = RoundedCornerShape(50.dp),
            colors = CardDefaults.cardColors(
                // Use MistNavy with opacity
                containerColor = MistNavy.copy(alpha = 0.90f)
            ),
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                tonalElevation = 0.dp,
                modifier = Modifier.height(65.dp)
            ) {
                items.forEach { item ->
                    val selected = if (item == BottomNavItem.Home) {
                        currentRoute?.startsWith("home") == true
                    } else {
                        currentRoute == item.route
                    }

                    val iconColor by animateColorAsState(
                        targetValue = if (selected) Color.White else Color.Gray.copy(alpha = 0.5f),
                        animationSpec = tween(durationMillis = 300), label = "iconColor"
                    )
                    val iconScale by animateFloatAsState(
                        targetValue = if (selected) 1.1f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "scale"
                    )
                    val dotScale by animateFloatAsState(
                        targetValue = if (selected) 1f else 0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy), label = "dot"
                    )

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            val targetRoute = if (item == BottomNavItem.Home) Route.home(userId) else item.route
                            if (!selected) {
                                navController.navigate(targetRoute) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                }
                            }
                        },
                        interactionSource = remember { MutableInteractionSource() },
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.animateContentSize()
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(26.dp).scale(iconScale),
                                    tint = iconColor
                                )
                                if (selected || dotScale > 0f) {
                                    Box(
                                        modifier = Modifier.padding(top = 4.dp).size(5.dp).scale(dotScale).alpha(dotScale).clip(CircleShape).background(Color.White)
                                    )
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}