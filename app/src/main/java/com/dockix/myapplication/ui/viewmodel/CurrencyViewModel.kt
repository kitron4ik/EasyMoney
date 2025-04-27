package com.dockix.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dockix.myapplication.data.api.CurrencyInfo
import com.dockix.myapplication.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {
    private val repository = CurrencyRepository()
    
    private val _usdToRubRate = MutableStateFlow<UiState<CurrencyInfo>>(UiState.Loading)
    val usdToRubRate: StateFlow<UiState<CurrencyInfo>> = _usdToRubRate.asStateFlow()
    
    init {
        loadCurrencyRate()
    }
    
    fun loadCurrencyRate() {
        viewModelScope.launch {
            _usdToRubRate.value = UiState.Loading
            repository.getUsdToRubRate().collectLatest { result ->
                _usdToRubRate.value = result.fold(
                    onSuccess = { UiState.Success(it) },
                    onFailure = { UiState.Error(it.message ?: "Unknown error") }
                )
            }
        }
    }
}

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
} 