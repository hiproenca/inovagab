package com.hig.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hig.inovagab.data.dto.DtoCreateIdeaRequest
import com.hig.inovagab.data.dto.DtoUpdateIdeaStatusRequest
import com.hig.inovagab.data.repository.ApiResult
import com.hig.inovagab.data.repository.IdeaRep
import com.hig.inovagab.data.repository.RepProvider
import com.hig.inovagab.data.utils.IdeaStatus
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.model.Idea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class IdeaUiState {
    data object Loading : IdeaUiState()
    data class Success(val ideas: List<Idea>) : IdeaUiState()
    data class Error(val message: String) : IdeaUiState()
}

class IdeaViewModel(private val rep: IdeaRep = RepProvider.ideaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<IdeaUiState>(IdeaUiState.Loading)
    val uiState: StateFlow<IdeaUiState> = _uiState.asStateFlow()

    fun loadIdeas(role: UserRole, userId: String) {
        viewModelScope.launch {
            _uiState.value = IdeaUiState.Loading
            when (val result = rep.getIdeas()) {
                is ApiResult.Success -> {
                    val ideas = if (role == UserRole.OPERATOR) {
                        result.data.filter { it.authorId == userId }
                    } else {
                        result.data
                    }
                    _uiState.value = IdeaUiState.Success(ideas)
                }
                is ApiResult.Error -> {
                    _uiState.value = IdeaUiState.Error(result.exception)
                }
            }
        }
    }

    fun submitIdeas(title: String, description: String, authorId: String, role: UserRole,  strategyId: String? = null) {
        viewModelScope.launch {
            val request = DtoCreateIdeaRequest(
                title = title,
                description = description,
                authorId = authorId,
                strategyId = strategyId
            )

            when (val result = rep.createIdea(request)) {
                is ApiResult.Success -> loadIdeas(role, authorId)
                is ApiResult.Error -> {
                    _uiState.value = IdeaUiState.Error("Falha ao criar ideia: ${result.exception}")
                }
            }
        }
    }

    fun updateIdeaStatus(ideaId: String, newStatus: IdeaStatus, role: UserRole, userId: String) {
        viewModelScope.launch {
            when (val result = rep.updateIdeaStatus(ideaId, DtoUpdateIdeaStatusRequest(newStatus))) {
                is ApiResult.Success -> loadIdeas(role, userId)
                is ApiResult.Error -> {
                    _uiState.value = IdeaUiState.Error("Falha ao atualizar status da ideia: ${result.exception}")
                }
            }
        }
    }


    fun deleteIdea(ideaId: String, role: UserRole, userId: String) {
        viewModelScope.launch {
            when (val result = rep.deleteIdea(ideaId)) {
                is ApiResult.Success -> loadIdeas(role, userId)
                is ApiResult.Error -> {
                    _uiState.value = IdeaUiState.Error("Falha ao excluir ideia: ${result.exception}")
                }
            }
        }
    }



}