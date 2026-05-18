package com.zano.mistcafe.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zano.mistcafe.Route
import com.zano.mistcafe.db.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    // "isLoading" state to show a white screen while checking
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    // Where should we start? Login or Home?
    private val _startDestination = mutableStateOf(Route.LOGIN)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            tokenManager.userId.collect { id ->
                if (id != null) {
                    _startDestination.value = Route.home(id)
                } else {
                    _startDestination.value = Route.LOGIN
                }
                _isLoading.value = false
            }
        }
    }
}