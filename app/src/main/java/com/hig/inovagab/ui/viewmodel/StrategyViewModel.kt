package com.hig.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hig.inovagab.data.dto.DtoCreateStrategyRequest
import com.hig.inovagab.data.repository.ApiResult
import com.hig.inovagab.data.repository.RepProvider
import com.hig.inovagab.data.repository.StrategyRep
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.model.Strategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class StrategyUiState {
    data object Loading : StrategyUiState()
    data class Success(val strategies: List<Strategy>) : StrategyUiState()
    data class Error(val message: String) : StrategyUiState()
}

class StrategyViewModel(
    private val rep: StrategyRep = RepProvider.strategyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<StrategyUiState>(StrategyUiState.Loading)
    val uiState: StateFlow<StrategyUiState> = _uiState.asStateFlow()

    fun loadStrategies(role: UserRole, userId: String) {
        viewModelScope.launch {
            _uiState.value = StrategyUiState.Loading
            when (val result = rep.getStrategies()) {
                is ApiResult.Success -> {
                    _uiState.value = StrategyUiState.Success(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = StrategyUiState.Error(result.exception)
                }
            }
        }
    }

    fun createStrategy(
        title: String,
        leaderId: String,
        category: String,
        campaign: String,
        date: String,
        description: String,
        role: UserRole,
        userId: String
    ) {
        viewModelScope.launch {
            val request = DtoCreateStrategyRequest(
                title = title,
                leaderId = leaderId,
                category = category,
                campaign = campaign,
                date = date,
                description = description
            )
            when (val result = rep.createStrategy(request)) {
                is ApiResult.Success -> loadStrategies(role, userId)
                is ApiResult.Error -> {
                    _uiState.value = StrategyUiState.Error("Falha ao criar estratégia: ${result.exception}")
                }
            }
        }
    }

    fun updateStrategy(
        strategyId: String,
        title: String,
        leaderId: String,
        category: String,
        campaign: String,
        date: String,
        description: String,
        role: UserRole,
        userId: String
    ) {
        viewModelScope.launch {
            val request = DtoCreateStrategyRequest(
                title = title,
                leaderId = leaderId,
                category = category,
                campaign = campaign,
                date = date,
                description = description
            )
            when (val result = rep.updateStrategy(strategyId, request)) {
                is ApiResult.Success -> loadStrategies(role, userId)
                is ApiResult.Error -> {
                    _uiState.value = StrategyUiState.Error("Falha ao atualizar estratégia: ${result.exception}")
                }
            }
        }
    }

    fun deleteStrategy(strategyId: String, role: UserRole, userId: String) {
        viewModelScope.launch {
            when (val result = rep.deleteStrategy(strategyId)) {
                is ApiResult.Success -> loadStrategies(role, userId)
                is ApiResult.Error -> {
                    _uiState.value = StrategyUiState.Error("Falha ao excluir estratégia: ${result.exception}")
                }
            }
        }
    }
}