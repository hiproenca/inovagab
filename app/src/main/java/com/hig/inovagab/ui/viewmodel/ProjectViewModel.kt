package com.hig.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hig.inovagab.data.repository.ProjectRep
import com.hig.inovagab.data.repository.RepProvider
import com.hig.inovagab.model.Project
import com.hig.inovagab.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProjectUiState {
    data object Loading : ProjectUiState()
    data class Success(val projects: List<Project>) : ProjectUiState()
    data class Error(val message: String) : ProjectUiState()
}

class ProjectViewModel(
    private val rep: ProjectRep = RepProvider.projectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProjectUiState>(ProjectUiState.Loading)
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    fun loadProjects(role: UserRole, userId: String) {
        viewModelScope.launch {
            _uiState.value = ProjectUiState.Loading
            try {
                val projects = rep.getAllProjects()
                _uiState.value = ProjectUiState.Success(projects)
            } catch (e: Exception) {
                _uiState.value = ProjectUiState.Error(
                    e.message ?: "Erro ao carregar os projetos."
                )
            }
        }
    }
}