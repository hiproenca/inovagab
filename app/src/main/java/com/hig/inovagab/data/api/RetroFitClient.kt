package com.hig.inovagab.data.api

import com.hig.inovagab.model.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AguiaBrancaApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/strategies")
    suspend fun getStrategies(@Header("Authorization") token: String): List<Strategy>

    @GET("api/ideas")
    suspend fun getIdeas(@Header("Authorization") token: String): List<Idea>

    @GET("api/projects")
    suspend fun getProjects(@Header("Authorization") token: String): List<Project>
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