package com.hig.inovagab.data.repository

import com.hig.inovagab.data.dto.DtoDashboardResponse
import com.hig.inovagab.data.dto.DtoGeminiInsightResponse

interface DashboardRep {
    suspend fun getDashboardSummary(): ApiResult<DtoDashboardResponse>
    suspend fun getDashboardInsights(): ApiResult<DtoGeminiInsightResponse>
}