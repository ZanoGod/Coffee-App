package com.zano.mistcafe.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zano.mistcafe.ui.components.*
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        // 1. [REMOVED] Default snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontWeight = FontWeight.Bold,
                        fontFamily = myFonts,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MistNavy
                )
            )
        }
    ) { padding ->

        // 2. [NEW] Wrap content in Box to overlay the Custom Snackbar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
            ) {

                // --- APPEARANCE ---
                item { SettingsSection("Appearance") }

                item {
                    ThemeSwitchItem(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isChecked ->
                            onThemeChanged(isChecked)
                        }
                    )
                }

                // --- ACCOUNT ---
                item { SettingsSection("Account") }
                item {
                    SettingsNavItem(
                        icon = Icons.Default.LocationOn,
                        title = "Saved Addresses",
                        onClick = { /* TODO */ }
                    )
                }

                // --- DATA ---
                item { SettingsSection("Data") }
                item {
                    SettingsActionItem(
                        icon = Icons.Default.DeleteOutline,
                        title = "Clear Cache",
                        isDestructive = true,
                        onClick = {
                            scope.launch {
                                // Trigger the custom snackbar
                              //  snackbarHostState.showSnackbar("Cache cleared successfully")
                                val job = launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Cache cleared successfully",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                    )
                }

                // --- SUPPORT ---
                item { SettingsSection("Support") }
                item { SettingsNavItem(Icons.AutoMirrored.Filled.Help, "Help / FAQ") {} }
                item { SettingsNavItem(Icons.Default.Email, "Contact Us") {} }
                item { SettingsNavItem(Icons.Default.Policy, "Privacy Policy") {} }
                item { SettingsNavItem(Icons.Default.Description, "Terms & Conditions") {} }

                item { AppVersionItem() }
            }

            // 3. [NEW] Add AnimatedSnackbarHost at the top
            AnimatedSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 13.dp) // Small padding from top
            )
        }
    }
}