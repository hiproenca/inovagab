package com.hig.inovagab.data.repository

import com.hig.inovagab.data.api.AguiaBrancaApi
import com.hig.inovagab.data.dto.DtoCreateProjectRequest
import com.hig.inovagab.data.repository.SafeApiCall.execute
import com.hig.inovagab.model.Project

class ProjectRepImpl(private val api: AguiaBrancaApi) : ProjectRep {

    override suspend fun getProjects(): ApiResult<List<Project>> =
        execute { api.getProjects() }

    override suspend fun createProject(request: DtoCreateProjectRequest): ApiResult<Project> =
        execute { api.createProject(request) }

    override suspend fun updateProject(id: String, project: Project): ApiResult<Project> =
        execute { api.updateProject(id, project) }

    override suspend fun deleteProject(id: String): ApiResult<Unit> =
        execute { api.deleteProject(id); Unit }
}