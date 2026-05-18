package com.zano.mistcafe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zano.mistcafe.api.CoffeeApiService
import com.zano.mistcafe.db.CartItem
import com.zano.mistcafe.db.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(
    private val api: CoffeeApiService,
    private val tokenManager: TokenManager // 1. Inject TokenManager
) : ViewModel() {

    var isNavigating by mutableStateOf(false)
        private set

    // 2. State to hold the current User ID
    var currentUserId by mutableStateOf(0)
        private set

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set

    var isInitialLoading by mutableStateOf(true)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        // 3. Automatically load cart when ViewModel starts
        loadUserAndCart()
    }

    private fun loadUserAndCart() {
        viewModelScope.launch {
            isInitialLoading = true
            // Get userId from DataStore
            val userId = tokenManager.userId.first() ?: 0
            currentUserId = userId

            if (userId != 0) {
                try {
                    cartItems = api.getCart(userId)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            isInitialLoading = false
        }
    }

    fun refreshCart() {
        if (currentUserId == 0) return

        viewModelScope.launch {
            isRefreshing = true
            try {
                cartItems = api.getCart(currentUserId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isRefreshing = false
        }
    }

    fun removeItem(cartId: Int) {
        viewModelScope.launch {
            api.removeCartItem(cartId)
            refreshCart() // Reload using the stored currentUserId
        }
    }

    fun startNavigation() {
        isNavigating = true
    }

    fun formattedTotalPrice(): String {
        val total = cartItems.sumOf { it.price * it.quantity }
        return "%.2f".format(total)
    }


    // Add this function inside CartViewModel
    fun clearCart(userId: Int) {
        viewModelScope.launch {
            try {
                // Method 1: If your API has a specific endpoint to clear everything
                // api.deleteAllCartItems(userId)

                // Method 2: (Fallback) Loop through the list and delete one by one
                cartItems.forEach { item ->
                    api.removeCartItem(item.id)
                }

                // Update UI immediately
                cartItems = emptyList()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}