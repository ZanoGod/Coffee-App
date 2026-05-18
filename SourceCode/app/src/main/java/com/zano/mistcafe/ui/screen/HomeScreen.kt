package com.zano.mistcafe.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zano.mistcafe.db.Product
import com.zano.mistcafe.ui.components.*
import com.zano.mistcafe.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userId: Int,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState
    val isLoading by viewModel.isLoading
    val addingProductId by viewModel.addingProductId

    // Categories
    val categories = listOf("All", "HOT", "ICE", "CAKE", "MATCHA")
    var selectedCategory by remember { mutableStateOf("All") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        if (userId != 0) {
            viewModel.loadHome(userId)
        }
    }

    val onAddToCart: (Product) -> Unit = { product ->
        scope.launch {
            val success = viewModel.addToCart(product, userId)
            if (success) {
                val job = launch {
                    snackbarHostState.showSnackbar(
                        message = "${product.name} added",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        // 1. [FIX] Adaptive Background
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // --- 1. Header ---
                item {
                    CoffeeHeader(username = state.username.ifBlank { "Guest" })
                }

                // --- 2. Sticky Categories ---
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            // 2. [FIX] Adaptive Sticky Header Background
                            .background(MaterialTheme.colorScheme.background)
                            .padding(vertical = 2.dp)
                    ) {
                        CategoryTabs(
                            categories = categories,
                            selected = selectedCategory,
                            onCategorySelected = { selectedCategory = it }
                        )
                    }
                }

                // --- 3. Loading State ---
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BouncingDotsLoadingView("Brewing your menu...")
                        }
                    }
                } else {
                    // --- 4. Content Logic ---

                    if (selectedCategory == "All") {
                        // === Helper Function for Sections ===
                        fun section(title: String, products: List<Product>) {
                            if (products.isNotEmpty()) {
                                item {
                                    ProductSection(
                                        title = title,
                                        products = products,
                                        addingProductId = addingProductId,
                                        onFavoriteClick = { viewModel.toggleFavorite(it.id, userId) },
                                        onAddToCart = onAddToCart
                                    )
                                }
                            }
                        }

                        section("Popular Hot Coffee", state.hotProducts)
                        section("Popular Ice Coffee", state.iceProducts)
                        section("Matcha Specials", state.matchaProducts)
                        section("Cake Specials", state.cakeProducts)

                    } else {
                        // === VIEW: SPECIFIC CATEGORY GRID ===
                        val filteredProducts = when (selectedCategory) {
                            "HOT" -> state.hotProducts
                            "ICE" -> state.iceProducts
                            "MATCHA" -> state.matchaProducts
                            "CAKE" -> state.cakeProducts
                            else -> emptyList()
                        }

                        if (filteredProducts.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No items found in $selectedCategory",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        } else {
                            // Manual Grid for LazyColumn
                            items(filteredProducts.chunked(2)) { rowItems ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    for (product in rowItems) {
                                        Box(modifier = Modifier.weight(1f)) {
                                            ProductCard(
                                                product = product,
                                                isAdding = addingProductId == product.id,
                                                onAddToCart = { onAddToCart(product) },
                                                onFavoriteClick = { viewModel.toggleFavorite(product.id, userId) }
                                            )
                                        }
                                    }
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- 5. Bottom Navigation ---
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomNavigationBar(navController, userId)
            }

            // --- 6. Snackbar ---
            AnimatedSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 50.dp)
            )
        }
    }
}