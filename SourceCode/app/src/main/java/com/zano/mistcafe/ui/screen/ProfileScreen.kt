package com.zano.mistcafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zano.mistcafe.Route
import com.zano.mistcafe.db.UserProfile
import com.zano.mistcafe.ui.components.BottomNavigationBar
import com.zano.mistcafe.ui.components.BouncingDotsLoadingView
import com.zano.mistcafe.ui.theme.MistCream
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts
import com.zano.mistcafe.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userId = viewModel.currentUserId

    Scaffold(
        // Adaptive Background
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { ProfileTopBar() },
        bottomBar = { BottomNavigationBar(navController, userId) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                viewModel.isLoading -> {
                    BouncingDotsLoadingView("Loading Profile...")
                }

                viewModel.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = viewModel.error ?: "Error", color = Color.Red)
                    }
                }

                viewModel.profile != null -> {
                    ModernProfileContent(
                        profile = viewModel.profile!!,
                        onLogout = {
                            viewModel.logout {
                                navController.navigate(Route.LOGIN) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        },
                        onCartClick = {
                            navController.navigate(Route.CART) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onHistoryClick = { navController.navigate(Route.HISTORY) },
                        onSettingsClick = { navController.navigate(Route.SETTINGS) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(4.dp),
        title = {
            Text(
                "My Profile",
                fontWeight = FontWeight.Bold,
                fontFamily = myFonts,
                color = Color.White,
                fontSize = 22.sp,
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            // Always Brand Navy
            containerColor = MistNavy
        )
    )
}

@Composable
fun ModernProfileContent(
    profile: UserProfile,
    onLogout: () -> Unit,
    onCartClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // --- Profile Card ---
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(
                // Adaptive Surface (White vs Dark Navy)
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar Area
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .border(2.dp, MistNavy, CircleShape)
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(MistCream),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Text Info
                Column {
                    Text(
                        text = profile.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = myFonts,
                        // Adaptive Text
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = profile.email,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Menu Options ---
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileMenuItem(
                icon = Icons.Default.ShoppingBag,
                title = "My Cart",
                onClick = onCartClick
            )

            ProfileMenuItem(
                icon = Icons.Default.History,
                title = "History",
                onClick = onHistoryClick
            )

            ProfileMenuItem(
                icon = Icons.Default.Settings,
                title = "Settings",
                onClick = onSettingsClick
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Logout Button ---
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.1f),
                contentColor = Color.Red
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(imageVector = Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Log Out", fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = myFonts)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            // [FIX] Use Surface (Adapts to Dark Mode)
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // [FIX] Icon Background
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    // Use Primary with low alpha (Navy tint in Light, Beige tint in Dark)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                // [FIX] Icon Tint: Use Primary color (Navy in Light, Beige in Dark)
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // [FIX] Text Color: Use onSurface
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = myFonts,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}