package com.zano.mistcafe.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zano.mistcafe.api.CoffeeRepository
import com.zano.mistcafe.db.TokenManager
import com.zano.mistcafe.db.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CoffeeRepository,
    private val tokenManager: TokenManager // 2. Inject TokenManager
) : ViewModel() {

    var favorites by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(true) // Start true to show loading immediately
        private set

    // 3. Store the logged-in User ID
    var currentUserId by mutableStateOf(0)
        private set

    init {
        // 4. Automatically load data when screen opens
        loadUserAndFavorites()
    }

    private fun loadUserAndFavorites() {
        viewModelScope.launch {
            isLoading = true
            // Get ID from DataStore
            val userId = tokenManager.userId.first() ?: 0
            currentUserId = userId

            if (userId != 0) {
                try {
                    favorites = repository.getFavoriteProducts(userId)
                } catch (e: Exception) {
                    Log.e("FAV_VM", e.message ?: "")
                }
            }
            isLoading = false
        }
    }

    // Public method to refresh manually if needed
    fun loadFavorites(userId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                favorites = repository.getFavoriteProducts(userId)
            } catch (e: Exception) {
                Log.e("FAV_VM", e.message ?: "")
            } finally {
                isLoading = false
            }
        }
    }

    fun removeFavorite(productId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                // 5. OPTIMISTIC UPDATE: Remove from UI immediately for speed
                favorites = favorites.filter { it.id != productId }

                // Then tell the server
                repository.toggleFavorite(userId, productId)

                // No need to reload logic here, as we already filtered the list
            } catch (e: Exception) {
                Log.e("FAV_REMOVE", e.message ?: "")
                // If error, reload to revert changes
                loadFavorites(userId)
            }
        }
    }
}