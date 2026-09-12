package com.hig.inovagab.data.repository

import com.hig.inovagab.model.Idea
import com.hig.inovagab.model.IdeaStatus
import kotlinx.coroutines.delay
import java.util.UUID

class FakeIdeaRep : IdeaRep {
    private val ideas = mutableListOf(
        Idea(id = "idea-1", title = "Reduzir tempo de conferência de carga", description = "Otimizar o checklist no pátio.", status = IdeaStatus.PENDING, authorId = "operator-current"),
        Idea(id = "idea-2", title = "Checklist digital pro motorista", description = "Substituir o papel por formulário no app.", status = IdeaStatus.APPROVED, authorId = "operator-current")
    )

    override suspend fun getIdeasByAuthor(authorId: String): List<Idea> {
        delay(300)
        return ideas.filter { it.authorId == authorId }
    }

    override suspend fun getAllIdeas(): List<Idea> {
        delay(300)
        return ideas.toList()
    }

    override suspend fun createIdea(idea: Idea): Idea {
        delay(300)
        val newIdea = idea.copy(id = UUID.randomUUID().toString())
        ideas.add(newIdea)
        return newIdea
    }

    override suspend fun updateIdeaStatus(ideaId: String, status: IdeaStatus): Idea {
        delay(300)
        val index = ideas.indexOfFirst { it.id == ideaId }
        require(index != -1) { "Ideia não encontrada: $ideaId" }
        val updated = ideas[index].copy(status = status)
        ideas[index] = updated
        return updated
    }
}