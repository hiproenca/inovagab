package com.hig.inovagab.data.dto

data class DtoDashboardResponse(
    val totalProjects: Int,
    val totalInvestment: Double,
    val totalFinancialReturn: Double,
    val totalProfit: Double,
    val globalRoiPercentage: Double
)
