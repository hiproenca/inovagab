package com.hig.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hig.inovagab.data.repository.RepProvider
import com.hig.inovagab.data.repository.StrategyRep
import com.hig.inovagab.model.Strategy
import com.hig.inovagab.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class StrategyUiState {
    data object Loading : StrategyUiState()
    data class Success(val strategies: List<Strategy>) : StrategyUiState()
    data class Error(val message: String) : StrategyUiState()
}

class StrategyViewModel(private val rep: StrategyRep = RepProvider.strategyRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<StrategyUiState>(StrategyUiState.Loading)
    val uiState: StateFlow<StrategyUiState> = _uiState.asStateFlow()

    fun loadStrategies(role: UserRole, userId: String) {
        viewModelScope.launch {
            _uiState.value = StrategyUiState.Loading
            try {
                val strategies = rep.getAllStrategies()
                _uiState.value = StrategyUiState.Success(strategies)
            } catch (e: Exception) {
                _uiState.value = StrategyUiState.Error(
                    e.message ?: "Erro ao carregar as estratégias."
                )
            }
        }
    }

}