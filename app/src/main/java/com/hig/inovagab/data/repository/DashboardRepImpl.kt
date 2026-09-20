package com.hig.inovagab.data.repository

import com.hig.inovagab.data.api.AguiaBrancaApi
import com.hig.inovagab.data.dto.DtoDashboardResponse
import com.hig.inovagab.data.dto.DtoGeminiInsightResponse
import com.hig.inovagab.data.repository.SafeApiCall.execute

class DashboardRepImpl(private val api: AguiaBrancaApi) : DashboardRep  {
    override suspend fun getDashboardSummary(): ApiResult<DtoDashboardResponse> =
        execute { api.getDashboardSummary() }

    override suspend fun getDashboardInsights(): ApiResult<DtoGeminiInsightResponse> =
        execute { api.getDashboardInsights() }

}