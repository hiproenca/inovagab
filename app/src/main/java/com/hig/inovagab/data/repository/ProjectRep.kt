package com.hig.inovagab.data.repository

import com.hig.inovagab.model.Project

interface ProjectRep {
    suspend fun getAllProjects (): List<Project>
    suspend fun createProject(project: Project): Project
    suspend fun updateProject(project: Project): Project
}