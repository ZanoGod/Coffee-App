package com.zano.mistcafe.viewmodel

import androidx.lifecycle.ViewModel
import com.zano.mistcafe.api.CoffeeRepository
import com.zano.mistcafe.db.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: CoffeeRepository
) : ViewModel() {

    suspend fun saveOrder(
        userId: Int,
        cartItems: List<CartItem>
    ): Boolean {
        println("CHECKOUT → cart size = ${cartItems.size}")

        if (cartItems.isEmpty()) {
            println("CHECKOUT → cart empty, aborting")
            return false
        }

        val result = repository.saveCompletedProducts(userId, cartItems)
        println("CHECKOUT → API success = $result")

        return result
    }

}
