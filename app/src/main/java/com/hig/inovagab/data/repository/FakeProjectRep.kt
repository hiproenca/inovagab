package com.hig.inovagab.data.repository

import com.hig.inovagab.model.Project
import com.hig.inovagab.model.ProjectStage
import com.hig.inovagab.model.ProjectStatus
import kotlinx.coroutines.delay
import java.util.UUID

class FakeProjectRep : ProjectRep {
    private val projects = mutableListOf(
        Project(
            id = "project-1",
            title = "Digitalização do checklist de frota",
            stage = ProjectStage.EXECUTION,
            status = ProjectStatus.ACTIVE,
            investment = 45000.0,
            deadline = "2026-12-01",
            financialReturn = 0.0
        )
    )

    override suspend fun getAllProjects(): List<Project> {
        delay(300)
        return projects.toList()
    }

    override suspend fun createProject(project: Project): Project {
        delay(300)
        val newProject = project.copy(id = UUID.randomUUID().toString())
        projects.add(newProject)
        return newProject
    }

    override suspend fun updateProject(project: Project): Project {
        delay(300)
        val index = projects.indexOfFirst { it.id == project.id }
        require(index != -1) { "Projeto não encontrado: ${project.id}" }
        projects[index] = project
        return project
    }
}