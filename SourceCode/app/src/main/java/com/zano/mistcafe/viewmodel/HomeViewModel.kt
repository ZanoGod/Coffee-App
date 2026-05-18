package com.zano.mistcafe.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zano.mistcafe.api.CoffeeRepository
import com.zano.mistcafe.db.HomeUiState
import com.zano.mistcafe.db.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CoffeeRepository,
    private val coffeeRepository: CoffeeRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(HomeUiState())
    val uiState: State<HomeUiState> = _uiState

    // 1. Add Loading State
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _addingProductId = mutableStateOf<Int?>(null)
    val addingProductId: State<Int?> = _addingProductId

// Inside HomeViewModel class

    fun loadHome(userId: Int) {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = coffeeRepository.getProfile(userId)   // ✅ fetch username
                val favIds = repository.getFavoriteIds(userId)

                val hot = repository.getProductsByCategory("hot")
                val ice = repository.getProductsByCategory("ice")
                val matcha = repository.getProductsByCategory("matcha")
                val cake = repository.getProductsByCategory("cake")

                _uiState.value = _uiState.value.copy(
                    username = user.name,
                    hotProducts = hot.map { it.copy(isFavorite = favIds.contains(it.id)) },
                    iceProducts = ice.map { it.copy(isFavorite = favIds.contains(it.id)) },
                    matchaProducts = matcha.map { it.copy(isFavorite = favIds.contains(it.id)) },
                    cakeProducts = cake.map { it.copy(isFavorite = favIds.contains(it.id)) }
                )

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }


    // ... addToCart and toggleFavorite remain the same ...
    suspend fun addToCart(product: Product, userId: Int): Boolean {
        return try {
            _addingProductId.value = product.id
            repository.addToCart(userId, product)
        } finally {
            _addingProductId.value = null
        }
    }

    fun toggleFavorite(productId: Int, userId: Int) {
        viewModelScope.launch {
            val response = repository.toggleFavorite(userId, productId)
            if (response.success) {
                _uiState.value = _uiState.value.copy(
                    hotProducts = _uiState.value.hotProducts.map {
                        if (it.id == productId) it.copy(isFavorite = response.is_favorite) else it
                    },
                    iceProducts = _uiState.value.iceProducts.map {
                        if (it.id == productId) it.copy(isFavorite = response.is_favorite) else it
                    },
                    matchaProducts = _uiState.value.matchaProducts.map {
                        if (it.id == productId) it.copy(isFavorite = response.is_favorite) else it
                    },
                    cakeProducts = _uiState.value.cakeProducts.map {
                        if (it.id == productId) it.copy(isFavorite = response.is_favorite) else it
                    }
                )
            }
        }
    }
}