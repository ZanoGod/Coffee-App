package com.zano.mistcafe.db

import com.google.gson.annotations.SerializedName


data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Double,
    @SerializedName("image")
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val category: String
)


data class HomeUiState(
    val username: String = "",
    val hotProducts: List<Product> = emptyList(),
    val iceProducts: List<Product> = emptyList(),
    val matchaProducts: List<Product> = emptyList(),
    val cakeProducts: List<Product> = emptyList()
)


data class CartItem(
    val id: Int,
    val product_id: Int,
    val product_name: String,
    val price: Double,
    val quantity: Int,
    val image_url: String
)


data class ApiResponse(
    val success: Boolean
)

data class FavoriteResponse(
    val success: Boolean,
    val is_favorite: Boolean
)


data class UserProfile(
    val id: Int,
    val name: String,
    val email: String
)


data class AuthResponse(
    val success: Boolean,
    val message: String? = null,
    val user_id: Int? = null
)


data class OrderHistoryItem(
    val id: Int,
    val userId: Int,
    val totalAmount: Double,
    val status: String,
    val orderDate: String,
    val itemsSummary: String
)
