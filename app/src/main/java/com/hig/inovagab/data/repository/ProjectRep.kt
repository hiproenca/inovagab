package com.hig.inovagab.data.repository

import com.hig.inovagab.data.dto.DtoCreateProjectRequest
import com.hig.inovagab.data.dto.DtoUpdateProjectProgressRequest
import com.hig.inovagab.model.Project

interface ProjectRep {
    suspend fun getProjects(): ApiResult<List<Project>>
    suspend fun createProject(request: DtoCreateProjectRequest): ApiResult<Project>
    suspend fun updateProjectProgress(id: String, request: DtoUpdateProjectProgressRequest): ApiResult<Project>
    suspend fun deleteProject(id: String): ApiResult<Unit>
}