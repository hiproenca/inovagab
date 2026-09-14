package com.hig.inovagab.data.repository

import com.hig.inovagab.model.Strategy
import kotlinx.coroutines.delay
import java.util.UUID

class FakeStrategyRep : StrategyRep {
    private val strategies = mutableListOf(
        Strategy(
            id = "strategy-1",
            title = "Excelência operacional 2026",
            category = "Eficiência",
            campaign = "Operação Enxuta",
            date = "2026-01-15",
            description = "Estratégia de excelência operacional para o ano de 2026"
        ),
    )

    override suspend fun getAllStrategies(): List<Strategy> {
        delay(300)
        return strategies.toList()
    }

    override suspend fun createStrategy(strategy: Strategy): Strategy {
        delay(300)
        val newStrategy = strategy.copy(id = UUID.randomUUID().toString())
        strategies.add(newStrategy)
        return newStrategy
    }

    override suspend fun updateStrategy(strategy: Strategy): Strategy {
        delay(300)
        val index = strategies.indexOfFirst { it.id == strategy.id }
        require(index != -1) { "Estratégia não encontrada: ${strategy.id}" }
        strategies[index] = strategy
        return strategy
    }

    override suspend fun deleteStrategy(strategyId: String) {
        delay(300)
        strategies.removeAll { it.id == strategyId }
    }
}