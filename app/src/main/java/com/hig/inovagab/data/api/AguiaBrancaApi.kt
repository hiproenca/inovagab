package com.hig.inovagab.data.api

import com.hig.inovagab.data.dto.DtoAuthenticationRequest
import com.hig.inovagab.data.dto.DtoAuthenticationResponse
import com.hig.inovagab.data.dto.DtoCreateIdeaRequest
import com.hig.inovagab.data.dto.DtoCreateProjectRequest
import com.hig.inovagab.data.dto.DtoCreateStrategyRequest
import com.hig.inovagab.data.dto.DtoDashboardResponse
import com.hig.inovagab.data.dto.DtoGeminiInsightResponse
import com.hig.inovagab.data.dto.DtoRegisterRequest
import com.hig.inovagab.model.Idea
import com.hig.inovagab.model.Project
import com.hig.inovagab.model.Strategy
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AguiaBrancaApi {

    @POST("api/auth/authenticate")
    suspend fun login(@Body request: DtoAuthenticationRequest): DtoAuthenticationResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: DtoRegisterRequest): DtoAuthenticationResponse

    @GET("api/dashboard")
    suspend fun getDashboardSummary(): DtoDashboardResponse

    @GET("api/dashboard/insights")
    suspend fun getDashboardInsights(): DtoGeminiInsightResponse

    // --- IDEAS ---
    @GET("api/ideas")
    suspend fun getIdeas(): List<Idea>

    @GET("api/ideas/my-ideas")
    suspend fun getMyIdeas(): List<Idea>

    @POST("api/ideas")
    suspend fun createIdea(@Body request: DtoCreateIdeaRequest): Idea

    @PUT("api/ideas/{id}")
    suspend fun updateIdea(@Path("id") id: String, @Body idea: Idea): Idea

    @DELETE("api/ideas/{id}")
    suspend fun deleteIdea(@Path("id") id: String)

    // --- PROJECTS ---
    @GET("api/projects")
    suspend fun getProjects(): List<Project>

    @POST("api/projects")
    suspend fun createProject(@Body request: DtoCreateProjectRequest): Project

    @PUT("api/projects/{id}")
    suspend fun updateProject(@Path("id") id: String, @Body project: Project): Project

    @DELETE("api/projects/{id}")
    suspend fun deleteProject(@Path("id") id: String)

    // --- STRATEGIES ---
    @GET("api/strategies")
    suspend fun getStrategies(): List<Strategy>

    @POST("api/strategies")
    suspend fun createStrategy(@Body request: DtoCreateStrategyRequest): Strategy

    @PUT("api/strategies/{id}")
    suspend fun updateStrategy(@Path("id") id: String, @Body request: DtoCreateStrategyRequest): Strategy

    @DELETE("api/strategies/{id}")
    suspend fun deleteStrategy(@Path("id") id: String)

}