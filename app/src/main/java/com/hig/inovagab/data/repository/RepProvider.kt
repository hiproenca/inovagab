package com.hig.inovagab.data.repository

object RepProvider {
        val ideaRepository: IdeaRep = FakeIdeaRep()
        val projectRepository: ProjectRep = FakeProjectRep()
        val strategyRepository: StrategyRep = FakeStrategyRep()

}