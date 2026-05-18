package com.zano.mistcafe.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zano.mistcafe.ui.components.BottomNavigationBar
import com.zano.mistcafe.ui.components.BouncingDotsLoadingView
import com.zano.mistcafe.ui.components.ProductCard
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts
import com.zano.mistcafe.viewmodel.FavoritesViewModel
import com.zano.mistcafe.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val userId = viewModel.currentUserId
    val favorites = viewModel.favorites
    val isLoading = viewModel.isLoading
    val scope = rememberCoroutineScope()

    var minDelayPassed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        minDelayPassed = true
    }

    val showLoading = isLoading || !minDelayPassed

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Adaptive Background
        topBar = { FavoritesTopBar() }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {

            when {
                showLoading -> {
                    BouncingDotsLoadingView("Loading favorites...")
                }

                favorites.isEmpty() -> {
                    EmptyFavoritesState()
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            top = 20.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 120.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = favorites,
                            key = { it.id }
                        ) { product ->

                            Box(
                                modifier = Modifier.animateItem(
                                    fadeInSpec = tween(durationMillis = 500),
                                    fadeOutSpec = tween(durationMillis = 500),
                                    placementSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            ) {
                                ProductCard(
                                    product = product,
                                    isAdding = false,
                                    onFavoriteClick = {
                                        viewModel.removeFavorite(product.id, userId)
                                    },
                                    onAddToCart = {
                                        scope.launch {
                                            homeViewModel.addToCart(product, userId)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                BottomNavigationBar(navController, userId)
            }
        }
    }
}
// --- Components ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(4.dp),
        title = {
            Text(
                "My Favorites",
                fontWeight = FontWeight.Bold,
                fontFamily = myFonts,
                color = Color.White,
                fontSize = 22.sp
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MistNavy // Brand Color
        )
    )
}

@Composable
fun EmptyFavoritesState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No favorites yet",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = myFonts,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the ❤️ icon on items\nto save them for later!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}