package com.hig.inovagab.data.repository

import com.hig.inovagab.data.api.AguiaBrancaApi
import com.hig.inovagab.data.dto.DtoCreateStrategyRequest
import com.hig.inovagab.data.repository.SafeApiCall.execute
import com.hig.inovagab.model.Strategy

class StrategyRepImpl(private val api : AguiaBrancaApi): StrategyRep {
    override suspend fun getStrategies(): ApiResult<List<Strategy>> =
        execute { api.getStrategies() }

    override suspend fun createStrategy(request: DtoCreateStrategyRequest): ApiResult<Strategy> =
        execute { api.createStrategy(request) }

    override suspend fun updateStrategy(id: String, request: DtoCreateStrategyRequest): ApiResult<Strategy> =
        execute { api.updateStrategy(id, request) }

    override suspend fun deleteStrategy(id: String): ApiResult<Unit> =
        execute { api.deleteStrategy(id) }
}