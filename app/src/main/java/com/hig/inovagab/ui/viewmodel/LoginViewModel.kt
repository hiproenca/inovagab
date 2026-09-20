package com.hig.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hig.inovagab.data.dto.DtoAuthenticationRequest
import com.hig.inovagab.data.repository.ApiResult
import com.hig.inovagab.data.repository.AuthRep
import com.hig.inovagab.data.repository.RepProvider
import com.hig.inovagab.data.utils.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val role: UserRole, val userId: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(
    private val rep: AuthRep = RepProvider.authRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(request: DtoAuthenticationRequest) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val result = rep.login(request)) {
                is ApiResult.Success -> {
                    val user = result.data.user
                    val userId = user.id
                    _uiState.value = if (userId != null) {
                        LoginUiState.Success(user.role, userId)
                    } else {
                        LoginUiState.Error("Resposta inesperada do servidor.")
                    }
                }

                is ApiResult.Error -> {
                    _uiState.value = LoginUiState.Error(result.exception)
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}