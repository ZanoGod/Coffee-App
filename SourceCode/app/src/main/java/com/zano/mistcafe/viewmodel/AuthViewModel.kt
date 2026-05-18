package com.zano.mistcafe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zano.mistcafe.api.CoffeeRepository
import com.zano.mistcafe.db.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException // Make sure to import this!

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: CoffeeRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null // Clear previous errors
                val res = repository.register(name, email, password)

                if (res.success) {
                    onSuccess()
                } else {
                    error = res.message
                }
            } catch (e: HttpException) {
                // HEX: 409 errors land here. We must parse the custom message.
                error = parseErrorMessage(e)
            } catch (e: Exception) {
                // Other errors (Network off, timeout)
                error = e.message ?: "An unknown error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    fun login(email: String, password: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null // Clear previous errors
                val res = repository.login(email, password)

                // ✅ FIX: Check if res.user_id is not null
                if (res.success && res.user_id != null) {
                    tokenManager.saveUserId(res.user_id)
                    onSuccess(res.user_id)
                } else {
                    // Handle cases where login failed OR user_id was missing
                    error = res.message ?: "Login failed: No User ID returned"
                }
            } catch (e: HttpException) {
                error = parseErrorMessage(e)
            } catch (e: Exception) {
                error = e.message ?: "An unknown error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    // --- Helper function to extract "message" from error JSON ---
// --- Improved Helper function ---
    private fun parseErrorMessage(e: HttpException): String {
        // 1. Try to read the raw error text
        val errorBody = try {
            e.response()?.errorBody()?.string()
        } catch (io: Exception) {
            return "Error reading error body"
        }

        // 2. If empty, return the standard HTTP code
        if (errorBody.isNullOrEmpty()) {
            return "Server Error: ${e.code()}"
        }

        // 3. LOG THE RAW ERROR (Crucial Step!)
        // Look at your Logcat in Android Studio for the tag "AuthError"
        android.util.Log.e("AuthError", "Raw server response: $errorBody")

        return try {
            // 4. Try to parse it as JSON
            val json = JSONObject(errorBody)

            // Check for common error keys
            if (json.has("message")) {
                json.getString("message")
            } else if (json.has("error")) {
                json.getString("error")
            } else {
                "Unknown server error"
            }
        } catch (jsonException: Exception) {
            // 5. If it's NOT JSON (e.g., HTML or PHP warning), return the raw text
            // limiting it to 100 chars so it fits on screen
            if (errorBody.length > 100) {
                "Server returned invalid data: ${errorBody.take(100)}..."
            } else {
                errorBody
            }
        }
    }
}