package com.hig.inovagab.data.repository

import com.hig.inovagab.model.Idea
import com.hig.inovagab.model.IdeaStatus

interface IdeaRep {
    suspend fun getIdeasByAuthor(authorId: String): List<Idea>
    suspend fun getAllIdeas(): List<Idea>
    suspend fun createIdea(idea: Idea): Idea
    suspend fun updateIdeaStatus(ideaId: String, status: IdeaStatus): Idea
}