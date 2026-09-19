package com.hig.inovagab.data.dto

import com.hig.inovagab.data.utils.ProjectStage
import com.hig.inovagab.data.utils.ProjectStatus

data class UpdateProjectProgressRequest(
    val stage: ProjectStage,
    val status: ProjectStatus,
    val results: String?
)
