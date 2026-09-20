package com.hig.inovagab.data.repository

import android.content.Context
import com.hig.inovagab.data.api.AguiaBrancaApi
import com.hig.inovagab.data.api.RetrofitClient
import com.hig.inovagab.data.local.SessionManager

object RepProvider {

        private lateinit var appContext: Context

        fun init(context: Context) {
                appContext = context.applicationContext
        }

        private val api: AguiaBrancaApi by lazy { RetrofitClient.getApi(appContext) }
        private val sessionManager: SessionManager by lazy { SessionManager(appContext) }

        val authRepository: AuthRep by lazy { AuthRepImpl(api, sessionManager) }
        val ideaRepository: IdeaRep by lazy { IdeaRepImpl(api) }
        val projectRepository: ProjectRep by lazy { ProjectRepImpl(api) }
        val strategyRepository: StrategyRep by lazy { StrategyRepImpl(api) }
        val dashboardRepository: DashboardRep by lazy { DashboardRepImpl(api) }
}