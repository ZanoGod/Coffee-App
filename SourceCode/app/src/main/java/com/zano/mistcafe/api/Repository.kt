package com.zano.mistcafe.api

import android.util.Log
import com.zano.mistcafe.db.CartItem
import com.zano.mistcafe.db.FavoriteResponse
import com.zano.mistcafe.db.OrderHistoryItem
import com.zano.mistcafe.db.Product
import com.zano.mistcafe.db.UserProfile
import com.zano.mistcafe.network.toDomain
import jakarta.inject.Inject
import org.json.JSONArray
import org.json.JSONObject


class CoffeeRepository @Inject constructor(
    private val api: CoffeeApiService,
) {

    suspend fun getProductsByCategory(category: String): List<Product> {
        return api.getProductsByCategory(category)
    }


    suspend fun toggleFavorite(
        userId: Int,
        productId: Int,
    ): FavoriteResponse =
        api.toggleFavorite(userId, productId)


    suspend fun getFavoriteIds(userId: Int): Set<Int> =
        api.getFavoriteIds(userId).toSet()

    suspend fun getFavoriteProducts(userId: Int): List<Product> {
        val raw = api.getFavoriteProducts(userId)
        Log.d("FAV_RAW", raw.toString())

        return raw
            .map { it.toDomain() }
            .map { it.copy(isFavorite = true) }
    }


    suspend fun addToCart(
        userId: Int,
        product: Product,
    ): Boolean {
        val response = api.addToCart(
            userId = userId,
            productId = product.id,
            productName = product.name,
            price = product.price,
            imageUrl = product.imageUrl
        )
        Log.d("RAW_RESPONSE", response.toString())
        return response.success
    }


    suspend fun getProfile(userId: Int): UserProfile {
        return api.getProfile(userId)
    }

    suspend fun login(email: String, password: String) =
        api.login(email, password)

    suspend fun register(name: String, email: String, password: String) =
        api.register(name, email, password)


    suspend fun saveCompletedProducts(
        userId: Int,
        cartItems: List<CartItem>
    ): Boolean {

        val grouped = cartItems.groupBy { it.product_id }
        val productsJson = JSONArray().apply {
            grouped.forEach { (_, items) ->
                val first = items.first()
                val quantity = items.sumOf { it.quantity }
                val totalPrice = items.sumOf { it.price * it.quantity }

                put(
                    JSONObject().apply {

                        put("product_id", first.product_id)
                        put("product_name", first.product_name)
                        put("price", first.price)
                        put("quantity", quantity)
                        put("total_price", totalPrice)
                    }
                )
            }
        }.toString()

        val response = api.saveCompletedProducts(
            userId = userId,
            productsJson = productsJson
        )

        return response.success
    }
    suspend fun getUserOrders(userId: Int): List<OrderHistoryItem> {

        return api.getUserOrders(userId)
    }


}





