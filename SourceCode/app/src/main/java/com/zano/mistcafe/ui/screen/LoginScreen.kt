package com.zano.mistcafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zano.mistcafe.Route
import com.zano.mistcafe.ui.components.AnimatedSnackbarHost
import com.zano.mistcafe.ui.components.LoadingView
import com.zano.mistcafe.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    // --- State ---
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // --- Helpers ---
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // 1. Create the Host State for the Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // 2. Listen for errors from ViewModel and trigger Snackbar
    LaunchedEffect(viewModel.error) {
        viewModel.error?.let { errorMsg ->
            snackbarHostState.showSnackbar(errorMsg)
        }
    }

    // 3. Use a Box to layer the Snackbar OVER the content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MistCream)
            // Handle clearing focus when tapping background
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {

        // --- Main Content Layer ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ... (Your Existing Header Code) ...
            Text(
                text = "Mist Cafe",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = MistNavy
            )

            Text(
                text = "Welcome Back",
                fontSize = 16.sp,
                color = MistSteelBlue,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // ... (Your Existing Inputs) ...
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                shape = RoundedCornerShape(12.dp),
                colors = inputCustomButtons(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = inputCustomButtons(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            // --- Buttons ---
            Button(
                onClick = {
                    focusManager.clearFocus()
                    val cleanEmail = email.trim()
                    val cleanPassword = password.trim()

                    viewModel.login(cleanEmail, cleanPassword) { userId ->
                        navController.navigate(Route.home(userId)) {
                            popUpTo(Route.LOGIN) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MistNavy,
                    contentColor = MistCream
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (viewModel.isLoading) {
                    LoadingView()
                } else {
                    Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = {
                navController.navigate("register")
            }) {
                Text("Create account", color = MistSteelBlue)
            }

            // I removed the old Text error display since you have the Snackbar now
        }

        // --- Snackbar Layer (Top Overlay) ---
        // We place this LAST in the Box so it draws on top of everything
        AnimatedSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter) // Pin to top
                .statusBarsPadding()        // Respect notch/status bar
        )
    }
}