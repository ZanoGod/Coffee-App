@file:Suppress("DEPRECATION")

package com.zano.mistcafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.zano.mistcafe.Route
import com.zano.mistcafe.db.CartItem
import com.zano.mistcafe.network.ApiConfig
import com.zano.mistcafe.ui.components.BottomNavigationBar
import com.zano.mistcafe.ui.components.BouncingDotsLoadingView
import com.zano.mistcafe.ui.theme.MistCream
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts
import com.zano.mistcafe.viewmodel.CartViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val userId = viewModel.currentUserId
    val listState = rememberLazyListState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing)
    val cartItems = viewModel.cartItems

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Adaptive background
        topBar = { CartTopBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (viewModel.isInitialLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    BouncingDotsLoadingView("Loading Cart...")
                }
            } else {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.refreshCart() },
                    swipeEnabled = !viewModel.isNavigating,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (cartItems.isEmpty()) {
                        EmptyCartMessage()
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                            contentPadding = PaddingValues(top = 20.dp, bottom = 240.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(items = cartItems, key = { it.id }) { item ->
                                ModernCartItemRow(
                                    item = item,
                                    onRemove = { viewModel.removeItem(item.id) }
                                )
                            }
                        }
                    }
                }
            }

            if (!viewModel.isInitialLoading && cartItems.isNotEmpty()) {
                CartSummary(
                    total = viewModel.formattedTotalPrice(),
                    viewModel = viewModel,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 100.dp, start = 20.dp, end = 20.dp),
                    onCheckoutClick = {
                        navController.navigate(Route.CHECKOUT) { launchSingleTop = true }
                    }
                )
            }
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                BottomNavigationBar(navController, userId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(4.dp),
        title = {
            Text(
                "My Cart",
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
fun EmptyCartMessage() {
    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your cart is empty",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = myFonts,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Looks like you haven't added\nany coffee yet. ☕",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun ModernCartItemRow(item: CartItem, onRemove: () -> Unit) {
    val imageUrl = ApiConfig.IMAGE_BASE_URL + item.image_url

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(0.dp),
        // [FIX] Use Surface color (Adapts to Dark Mode)
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = item.product_name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MistCream)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // [FIX] Product Name: onSurface (White in Dark Mode)
                Text(
                    text = item.product_name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    fontFamily = myFonts,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Qty Badge
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant, // Adaptive gray
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = "Qty: ${item.quantity}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Price: Use Primary Brand Color
                Text(
                    text = "$ ${item.price}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.background(Color(0xFFFEECEB), CircleShape).size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Remove",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CartSummary(
    total: String,
    viewModel: CartViewModel,
    modifier: Modifier = Modifier,
    onCheckoutClick: () -> Unit,
) {
    var isProcessingCheckout by remember { mutableStateOf(false) }

    LaunchedEffect(isProcessingCheckout) {
        if (isProcessingCheckout) {
            viewModel.startNavigation()
            delay(1000)
            onCheckoutClick()
            isProcessingCheckout = false
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(10.dp),
        // [FIX] Use Surface Color
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // [FIX] Label color: onSurfaceVariant
                Text(
                    text = "Total Price",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    fontFamily = myFonts
                )

                // [FIX] Price Value: onSurface (White in Dark Mode)
                Text(
                    text = "$ $total",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = myFonts
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { isProcessingCheckout = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isProcessingCheckout,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MistNavy,
                    contentColor = Color.White
                )
            ) {
                if (isProcessingCheckout) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(text = "Checkout Now", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = myFonts)
                }
            }
        }
    }
}