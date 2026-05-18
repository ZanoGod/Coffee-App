package com.zano.mistcafe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zano.mistcafe.api.CoffeeRepository
import com.zano.mistcafe.db.TokenManager // Import your TokenManager
import com.zano.mistcafe.db.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: CoffeeRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    var profile by mutableStateOf<UserProfile?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    // 2. Store User ID
    var currentUserId by mutableStateOf(0)
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            val userId = tokenManager.userId.first() ?: 0
            currentUserId = userId

            if (userId != 0) {
                try {
                    profile = repository.getProfile(userId)
                } catch (e: Exception) {
                    error = e.message
                }
            } else {
                error = "User not found"
            }
            isLoading = false
        }
    }

    // 3. Logout Function
    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            tokenManager.clearUserId() // Clear DataStore
            onLogoutSuccess()
        }
    }
}