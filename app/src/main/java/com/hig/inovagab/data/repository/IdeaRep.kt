package com.hig.inovagab.data.repository

import com.hig.inovagab.data.dto.DtoCreateIdeaRequest
import com.hig.inovagab.data.dto.DtoUpdateIdeaStatusRequest
import com.hig.inovagab.model.Idea

interface IdeaRep {
    suspend fun getIdeas(): ApiResult<List<Idea>>
    suspend fun createIdea(request: DtoCreateIdeaRequest): ApiResult<Idea>
    suspend fun updateIdeaStatus(id: String, request: DtoUpdateIdeaStatusRequest): ApiResult<Idea>
    suspend fun deleteIdea(id: String): ApiResult<Unit>
}