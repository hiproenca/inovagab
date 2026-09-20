package com.hig.inovagab.data.repository
import com.hig.inovagab.data.dto.DtoCreateStrategyRequest
import com.hig.inovagab.model.Strategy

interface StrategyRep {
    suspend fun getStrategies(): ApiResult<List<Strategy>>
    suspend fun createStrategy(request: DtoCreateStrategyRequest): ApiResult<Strategy>
    suspend fun updateStrategy(id: String, request: DtoCreateStrategyRequest): ApiResult<Strategy>
    suspend fun deleteStrategy(id: String): ApiResult<Unit>
}
