package com.hig.inovagab.data.dto

data class DtoCreateIdeaRequest(
    val title: String,
    val description: String,
    val authorId: String,
    val strategyId: String?
)
