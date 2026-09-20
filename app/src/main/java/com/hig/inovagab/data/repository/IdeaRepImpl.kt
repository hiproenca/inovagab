package com.hig.inovagab.data.repository

import com.hig.inovagab.data.api.AguiaBrancaApi
import com.hig.inovagab.data.dto.DtoCreateIdeaRequest
import com.hig.inovagab.data.repository.SafeApiCall.execute
import com.hig.inovagab.model.Idea

class IdeaRepImpl(private val api: AguiaBrancaApi) : IdeaRep {

    override suspend fun getIdeas(): ApiResult<List<Idea>> =
        execute { api.getIdeas() }

    override suspend fun getMyIdeas(): ApiResult<List<Idea>> =
        execute { api.getMyIdeas() }

    override suspend fun createIdea(request: DtoCreateIdeaRequest): ApiResult<Idea> =
        execute { api.createIdea(request) }

    override suspend fun updateIdea(id: String, idea: Idea): ApiResult<Idea> =
        execute { api.updateIdea(id, idea) }

    override suspend fun deleteIdea(id: String): ApiResult<Unit> =
        execute { api.deleteIdea(id) }
}