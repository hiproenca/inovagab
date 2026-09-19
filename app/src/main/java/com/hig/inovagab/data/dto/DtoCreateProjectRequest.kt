package com.hig.inovagab.data.dto

data class DtoCreateProjectRequest(
    val title: String,
    val managerId: String,
    val investment: Double,
    val deadline: String,
    val description: String,
    val strategyId: String?
)
