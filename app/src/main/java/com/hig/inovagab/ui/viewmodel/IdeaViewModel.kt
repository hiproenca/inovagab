package com.hig.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hig.inovagab.data.repository.IdeaRep
import com.hig.inovagab.data.repository.RepProvider
import com.hig.inovagab.model.Idea
import com.hig.inovagab.model.IdeaStatus
import com.hig.inovagab.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class IdeaUiState {
    data object Loading : IdeaUiState()
    data class Success(val ideas: List<Idea>) : IdeaUiState()
    data class Error(val message: String) : IdeaUiState()
}

class IdeaViewModel (private val rep: IdeaRep = RepProvider.ideaRepository): ViewModel() {

    private val _uiState = MutableStateFlow<IdeaUiState>(IdeaUiState.Loading)
    val uiState: StateFlow<IdeaUiState> = _uiState.asStateFlow()

    fun loadIdeas(role: UserRole, userId: String) {
        viewModelScope.launch {
            _uiState.value = IdeaUiState.Loading
            try {
                val ideas = if (role == UserRole.OPERATOR) {
                    rep.getIdeasByAuthor(userId)
                } else {
                    rep.getAllIdeas()
                }
                _uiState.value = IdeaUiState.Success(ideas)
            } catch (e: Exception) {
                _uiState.value = IdeaUiState.Error(
                    e.message ?: "Erro ao carregar as suas ideias, tente novamente mais tarde"
                )
            }
        }
    }

    fun submitIdeas(title: String, description: String, authorId: String, role: UserRole){
        viewModelScope.launch {
            try{
                val newIdea = Idea(
                    id = null,
                    title = title,
                    description = description,
                    status = IdeaStatus.PENDING,
                    authorId = authorId
                )
                rep.createIdea(newIdea)
                loadIdeas(role, authorId)
            } catch (e: Exception) {
                _uiState.value = IdeaUiState.Error("Falha ao criar ideia: ${e.message}")
            }
        }
    } // A chave do submitIdeas fecha AQUI.

    // Agora o updateIdeaStatus é uma função independente
    fun updateIdeaStatus(ideaId: String, newStatus: IdeaStatus, role: UserRole, userId: String){
        viewModelScope.launch {
            try{
                rep.updateIdeaStatus(ideaId, newStatus)
                loadIdeas(role, userId) // Corrigido de authorId para userId
            } catch (e: Exception){
                _uiState.value = IdeaUiState.Error("Falha ao atualizar status da ideia: ${e.message}")
            }
        }
    }
}