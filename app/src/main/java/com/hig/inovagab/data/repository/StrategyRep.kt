package com.hig.inovagab.data.repository
import com.hig.inovagab.model.Strategy

interface StrategyRep {
    suspend fun getAllStrategies(): List<Strategy>
    suspend fun createStrategy(strategy: Strategy): Strategy
    suspend fun updateStrategy(strategy: Strategy): Strategy
    suspend fun deleteStrategy(strategyId: String)
}
