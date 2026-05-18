package com.zano.mistcafe

import SplashScreen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zano.mistcafe.ui.screen.*
import com.zano.mistcafe.viewmodel.MainViewModel

@Composable
fun Navigator(
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()

    val isLoading by mainViewModel.isLoading
    val startDestination by mainViewModel.startDestination

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SplashScreen()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            // ... (Your other routes: HOME, LOGIN, REGISTER, etc. remain the same) ...

            composable(
                route = Route.HOME_ROUTE,
                arguments = listOf(navArgument("userId") { type = NavType.IntType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: 0
                HomeScreen(navController = navController, userId = userId)
            }
            composable(Route.LOGIN) { LoginScreen(navController) }
            composable(Route.REGISTER) { RegisterScreen(navController) }
            composable(Route.CART) { CartScreen(navController = navController) }
            composable(Route.FAVORITES) { FavoritesScreen(navController = navController) }
            composable(Route.PROFILE) { ProfileScreen(navController = navController) }
            composable(Route.CHECKOUT) { CheckoutScreen(navController = navController) }
            composable(Route.HISTORY) { HistoryScreen(navController) }

            // 4. Pass Theme State to Settings Screen
            composable(Route.SETTINGS) {
                SettingsScreen(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onThemeChanged = onThemeChanged
                )
            }
        }
    }
}


object Route {
    const val HOME_ROUTE = "home/{userId}"
    fun home(userId: Int) = "home/$userId"
    const val CART = "cart"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val CHECKOUT = "checkout"
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val HISTORY = "history"

    const val  SETTINGS = "settings"
}