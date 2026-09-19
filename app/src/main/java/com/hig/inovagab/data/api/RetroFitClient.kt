package com.hig.inovagab.data.api

import com.hig.inovagab.model.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AguiaBrancaApi {
    //@POST("api/auth/login")
   // suspend fun login(@Body request: LoginRequest): LoginResponse

    //@POST("api/auth/register")
    //suspend fun register(@Body request: DtoRegisterRequest): DtoAuthenticationResponse

    //@POST("api/ideas")
    //suspend fun createIdea(@Body request: DtoIdeaRequest): Idea

    //@POST("api/projects")
    //suspend fun createProject(@Body request: DtoProjectRequest): Project

    //@GET("api/strategies")
    //suspend fun getStrategies(@Header("Authorization") token: String): List<Strategy>

    //@GET("api/ideas")
    //suspend fun getIdeas(@Header("Authorization") token: String): List<Idea>

    //@GET("api/projects")
    //suspend fun getProjects(@Header("Authorization") token: String): List<Project>

    //@GET("api/dashboard")
    //suspend fun getDashboardSummary(): DtoDashboardResponse // Mapear o DTO correspondente

    //@GET("api/dashboard/insights")
    //suspend fun getDashboardInsights(): InsightsResponse // Onde a IA do Gemini atua

    //@PUT("api/ideas/{id}/status")
    //suspend fun updateIdeaStatus(@Path("id") id: Long, @Body status: StatusRequest): Idea

    //@DELETE("api/ideas/{id}")
    //suspend fun deleteIdea(@Path("id") id: Long)

    //@DELETE("api/projects/{id}")
    //suspend fun deleteProject(@Path("id") id: Long)

    //@DELETE("api/strategies/{id}")
    //suspend fun deleteStrategy(@Path("id") id: Long)





}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: AguiaBrancaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AguiaBrancaApi::class.java)
    }
}