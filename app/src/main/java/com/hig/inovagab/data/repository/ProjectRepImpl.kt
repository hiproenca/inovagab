package com.hig.inovagab.data.repository

import com.hig.inovagab.data.api.AguiaBrancaApi
import com.hig.inovagab.data.dto.DtoCreateProjectRequest
import com.hig.inovagab.data.repository.SafeApiCall.execute
import com.hig.inovagab.model.Project
import com.hig.inovagab.data.dto.DtoUpdateProjectProgressRequest

class ProjectRepImpl(private val api:  AguiaBrancaApi ): ProjectRep {
    override suspend fun getProjects(): ApiResult<List<Project>> =
        execute { api.getProjects() }

    override suspend fun createProject(request: DtoCreateProjectRequest): ApiResult<Project> =
        execute { api.createProject(request) }

    override suspend fun updateProjectProgress(
        id: String,
        request: DtoUpdateProjectProgressRequest
    ): ApiResult<Project> =
        execute { api.updateProjectProgress(id, request) }

    override suspend fun deleteProject(id: String): ApiResult<Unit> =
        execute { api.deleteProject(id) }
}