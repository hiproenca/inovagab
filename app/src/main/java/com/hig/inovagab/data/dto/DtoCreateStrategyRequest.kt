package com.hig.inovagab.data.dto

data class DtoCreateStrategyRequest(
    val title: String,
    val leaderId: String,
    val category: String,
    val campaign: String,
    val date: String,
    val description: String
)
