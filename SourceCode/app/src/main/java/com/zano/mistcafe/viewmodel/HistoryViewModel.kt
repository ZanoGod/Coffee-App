package com.zano.mistcafe.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zano.mistcafe.api.CoffeeApiService
import com.zano.mistcafe.api.CoffeeRepository // Assuming you have a repo
import com.zano.mistcafe.db.OrderHistoryItem
import com.zano.mistcafe.db.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: CoffeeRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    var orders by mutableStateOf<List<OrderHistoryItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            isLoading = true
            val userId = tokenManager.userId.first() ?: 0
            if (userId != 0) {
                try {
                    // Assuming you added getUserOrders to your Repository
                    orders = repository.getUserOrders(userId)
                        .sortedByDescending { it.orderDate }


//                    orders = orders.sortedByDescending { it.id }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            isLoading = false
        }
    }
}