package com.hig.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hig.inovagab.data.dto.DtoCreateProjectRequest
import com.hig.inovagab.data.repository.ApiResult
import com.hig.inovagab.data.repository.ProjectRep
import com.hig.inovagab.data.repository.RepProvider
import com.hig.inovagab.data.utils.ProjectStage
import com.hig.inovagab.data.utils.ProjectStatus
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.model.Project
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
            when (val result = rep.getProjects()) {
                is ApiResult.Success -> {
                    _uiState.value = ProjectUiState.Success(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = ProjectUiState.Error(result.exception)
                }
            }
        }
    }

    fun createProject(
        title: String,
        managerId: String,
        investment: Double,
        deadline: String,
        description: String,
        role: UserRole,
        userId: String,
        strategyId: String? = null
    ) {
        viewModelScope.launch {
            val request = DtoCreateProjectRequest(
                title = title,
                managerId = managerId,
                investment = investment,
                deadline = deadline,
                description = description,
                strategyId = strategyId
            )
            when (val result = rep.createProject(request)) {
                is ApiResult.Success -> loadProjects(role, userId)
                is ApiResult.Error -> {
                    _uiState.value = ProjectUiState.Error("Falha ao criar projeto: ${result.exception}")
                }
            }
        }
    }

    fun updateProjectProgress(
        projectId: String,
        stage: ProjectStage,
        status: ProjectStatus,
        results: String?,
        role: UserRole,
        userId: String,
        financialReturn: Double? = null
    ) {
        val current = (_uiState.value as? ProjectUiState.Success)?.projects?.find { it.id == projectId }
            ?: return
        val updated = current.copy(
            stage = stage,
            status = status,
            results = results,
            financialReturn = financialReturn ?: current.financialReturn
        )
        viewModelScope.launch {
            when (val result = rep.updateProject(projectId, updated)) {
                is ApiResult.Success -> loadProjects(role, userId)
                is ApiResult.Error -> {
                    _uiState.value = ProjectUiState.Error("Falha ao atualizar projeto: ${result.exception}")
                }
            }
        }
    }

    fun deleteProject(projectId: String, role: UserRole, userId: String) {
        viewModelScope.launch {
            when (val result = rep.deleteProject(projectId)) {
                is ApiResult.Success -> loadProjects(role, userId)
                is ApiResult.Error -> {
                    _uiState.value = ProjectUiState.Error("Falha ao excluir projeto: ${result.exception}")
                }
            }
        }
    }
}