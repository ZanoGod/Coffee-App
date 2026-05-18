package com.zano.mistcafe.ui.screen

import android.util.Patterns
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch

// --- YOUR MIST CAFE PALETTE ---
val MistNavy = Color(0xFF213555)
val MistSteelBlue = Color(0xFF3E5879)
val MistSand = Color(0xFFD8C4B6)
val MistCream = Color(0xFFF5EFE7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    // --- Form State ---
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // --- UI State ---
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    // --- System Handlers ---
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    // 1. Snackbar Host State
    val snackbarHostState = remember { SnackbarHostState() }

    // 2. Listen for Server/ViewModel Errors
    LaunchedEffect(viewModel.error) {
        viewModel.error?.let { errorMsg ->
            snackbarHostState.showSnackbar(errorMsg)
        }
    }

    // 3. Wrap in Box to overlay Snackbar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MistCream)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Header Section ---
            Text(
                text = "Mist Cafe",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = MistNavy
            )

            Text(
                text = "Create your account",
                fontSize = 16.sp,
                color = MistSteelBlue,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // --- Inputs Section ---

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(12.dp),
                colors = inputCustomButtons(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = inputCustomButtons(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle visibility"
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = inputCustomButtons(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle visibility"
                        )
                    }
                },
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = inputCustomButtons(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            // --- Action Section ---

            Button(
                onClick = {
                    focusManager.clearFocus() // Hide keyboard

                    // --- 4. Robust Validation Logic ---
                    val cleanName = name.trim()
                    val cleanEmail = email.trim()
                    val cleanPass = password.trim()
                    val cleanConfirm = confirmPassword.trim()

                    when {
                        cleanName.isEmpty() -> {
                            scope.launch { snackbarHostState.showSnackbar("Please enter your full name.") }
                        }
                        cleanEmail.isEmpty() -> {
                            scope.launch { snackbarHostState.showSnackbar("Email cannot be empty.") }
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> {
                            scope.launch { snackbarHostState.showSnackbar("Please enter a valid email address.") }
                        }
                        cleanPass.length < 6 -> {
                            scope.launch { snackbarHostState.showSnackbar("Password must be at least 6 characters.") }
                        }
                        cleanPass != cleanConfirm -> {
                            scope.launch { snackbarHostState.showSnackbar("Passwords do not match!") }
                        }
                        else -> {
                            // All local checks passed, proceed to ViewModel
                            viewModel.register(cleanName, cleanEmail, cleanPass) {
                                navController.navigate(Route.LOGIN) {
                                    popUpTo(Route.REGISTER) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
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
                    Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Navigation to Login
            TextButton(onClick = {
                navController.navigate(Route.LOGIN) {
                    popUpTo(Route.REGISTER) { inclusive = true }
                    launchSingleTop = true
                }
            }) {
                Text("Already have an account? Login", color = MistSteelBlue)
            }
        }

        // --- Snackbar Overlay ---
        AnimatedSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter) // Appear from top
                .statusBarsPadding()
        )
    }
}

// Helper for consistent input styling
@Composable
fun inputCustomButtons() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MistNavy,
    unfocusedBorderColor = MistSand,
    focusedLabelColor = MistNavy,
    unfocusedLabelColor = MistSteelBlue,
    cursorColor = MistNavy,
    focusedTextColor = MistNavy,
    unfocusedTextColor = MistNavy,
    errorBorderColor = MaterialTheme.colorScheme.error,
    errorLabelColor = MaterialTheme.colorScheme.error
)