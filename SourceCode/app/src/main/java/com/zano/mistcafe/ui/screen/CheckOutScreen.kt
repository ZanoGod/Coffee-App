package com.zano.mistcafe.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.location.LocationServices
import com.zano.mistcafe.R
import com.zano.mistcafe.Route
import com.zano.mistcafe.ui.components.MiniMapCard
import com.zano.mistcafe.ui.components.OrderType
import com.zano.mistcafe.ui.components.PaymentMethod
import com.zano.mistcafe.ui.components.PaymentOption
import com.zano.mistcafe.ui.components.SegmentedOrderType
import com.zano.mistcafe.ui.theme.MistNavy
import com.zano.mistcafe.ui.theme.myFonts
import com.zano.mistcafe.viewmodel.CartViewModel
import com.zano.mistcafe.viewmodel.CheckoutViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var showSuccess by rememberSaveable { mutableStateOf(false) }

    val userId = cartViewModel.currentUserId
    val cartItems = cartViewModel.cartItems
    val isCartReady = cartItems.isNotEmpty()

    var selectedOrderType by remember { mutableStateOf(OrderType.PICKUP) }
    var selectedPayment by remember { mutableStateOf(PaymentMethod.CASH) }
    val scrollState = rememberScrollState()

    val cashPaymentTitle = if (selectedOrderType == OrderType.PICKUP) "Pay at Counter / Cash" else "Cash on Delivery"

    val shopLat = 1.3029898121539332
    val shopLong = 103.83512975789621
    var mapLat by rememberSaveable { mutableDoubleStateOf(shopLat) }
    var mapLong by rememberSaveable { mutableDoubleStateOf(shopLong) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    LaunchedEffect(Unit) {
        if (cartViewModel.cartItems.isEmpty()) cartViewModel.refreshCart()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let { mapLat = it.latitude; mapLong = it.longitude }
                }
            } catch (e: SecurityException) { e.printStackTrace() }
        }
    }

    LaunchedEffect(selectedOrderType) {
        if (selectedOrderType == OrderType.PICKUP) {
            mapLat = shopLat
            mapLong = shopLong
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location -> location?.let { mapLat = it.latitude; mapLong = it.longitude } }
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Adaptive
        topBar = { CheckoutTopBar(onBackClick = { navController.popBackStack() }) }
    ) { padding ->

        Box(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CheckoutSectionHeader("Order Type")
                    SegmentedOrderType(selected = selectedOrderType, onSelected = { selectedOrderType = it })
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CheckoutSectionHeader(if (selectedOrderType == OrderType.PICKUP) "Pickup Location" else "Delivery Address")
                    MiniMapCard(latitude = mapLat, longitude = mapLong)
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CheckoutSectionHeader("Payment Method")
                    PaymentOption(title = cashPaymentTitle, img = R.drawable.ic_cash, selected = selectedPayment == PaymentMethod.CASH) { selectedPayment = PaymentMethod.CASH }
                    PaymentOption(title = "Credit / Debit Card", img = R.drawable.ic_credit_card, selected = selectedPayment == PaymentMethod.CARD) { selectedPayment = PaymentMethod.CARD }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
                    .padding(24.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val success = checkoutViewModel.saveOrder(userId, cartItems)
                            if (success) {
                                cartViewModel.clearCart(userId)
                                showSuccess = true
                            }
                        }
                    },
                    enabled = isCartReady,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MistNavy,
                        disabledContainerColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth().height(56.dp).shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isCartReady) {
                        Text("Confirm Order", fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = myFonts)
                    } else {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    }
                }
            }

            if (showSuccess) {
                OrderSuccessOverlay(
                    visible = true,
                    onFinished = {
                        navController.navigate(Route.home(userId)) {
                            popUpTo(Route.LOGIN) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(4.dp),
        title = {
            Text("Checkout", fontWeight = FontWeight.Bold, fontFamily = myFonts, color = Color.White, fontSize = 22.sp)
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MistNavy)
    )
}

@Composable
fun CheckoutSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = myFonts,
        color = MaterialTheme.colorScheme.onBackground
    )
}
@Composable
fun OrderSuccessOverlay(visible: Boolean, onFinished: () -> Unit) {
    if (!visible) return
    var startExit by remember { mutableStateOf(false) }
    var hasNavigated by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_order))
    val animatedAlpha by animateFloatAsState(targetValue = if (startExit) 0f else 1f, animationSpec = tween(durationMillis = 500), label = "alpha")
    val animatedScale by animateFloatAsState(targetValue = if (startExit) 0.85f else 1f, animationSpec = tween(durationMillis = 500), label = "scale")

    LaunchedEffect(Unit) {
        delay(2000)
        startExit = true
        delay(500)
        if (!hasNavigated) { hasNavigated = true; onFinished() }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.graphicsLayer { alpha = animatedAlpha; scaleX = animatedScale; scaleY = animatedScale },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LottieAnimation(composition = composition, iterations = 1, modifier = Modifier.size(250.dp))
            Text(text = "Order Placed!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, fontFamily = myFonts, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}