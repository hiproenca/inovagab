package com.hig.inovagab.data.repository

import com.hig.inovagab.data.dto.DtoCreateIdeaRequest
import com.hig.inovagab.model.Idea

interface IdeaRep {
    suspend fun getIdeas(): ApiResult<List<Idea>>
    suspend fun getMyIdeas(): ApiResult<List<Idea>>
    suspend fun createIdea(request: DtoCreateIdeaRequest): ApiResult<Idea>
    suspend fun updateIdea(id: String, idea: Idea): ApiResult<Idea>
    suspend fun deleteIdea(id: String): ApiResult<Unit>
}