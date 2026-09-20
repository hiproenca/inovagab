package com.hig.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hig.inovagab.data.dto.DtoDashboardResponse
import com.hig.inovagab.data.dto.DtoGeminiInsightResponse
import com.hig.inovagab.data.repository.ApiResult
import com.hig.inovagab.data.repository.DashboardRep
import com.hig.inovagab.data.repository.RepProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val summary: DtoDashboardResponse) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

sealed class InsightUiState {
    data object Idle : InsightUiState()
    data object Loading : InsightUiState()
    data class Success(val insight: DtoGeminiInsightResponse) : InsightUiState()
    data class Error(val message: String) : InsightUiState()
}

class DashboardViewModel(
    private val rep: DashboardRep = RepProvider.dashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _insightState = MutableStateFlow<InsightUiState>(InsightUiState.Idle)
    val insightState: StateFlow<InsightUiState> = _insightState.asStateFlow()

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            when (val result = rep.getDashboardSummary()) {
                is ApiResult.Success -> {
                    _uiState.value = DashboardUiState.Success(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = DashboardUiState.Error(result.exception)
                }
            }
        }
    }

    fun loadInsight() {
        viewModelScope.launch {
            _insightState.value = InsightUiState.Loading
            when (val result = rep.getDashboardInsights()) {
                is ApiResult.Success -> {
                    _insightState.value = InsightUiState.Success(result.data)
                }
                is ApiResult.Error -> {
                    _insightState.value = InsightUiState.Error(result.exception)
                }
            }
        }
    }
}