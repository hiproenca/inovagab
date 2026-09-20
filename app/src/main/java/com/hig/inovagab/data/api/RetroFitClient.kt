package com.hig.inovagab.data.api

import android.content.Context
import com.hig.inovagab.BuildConfig
import com.hig.inovagab.data.local.SessionManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Volatile
    private var instance: AguiaBrancaApi? = null

    fun getApi(context: Context): AguiaBrancaApi {
        val appContext = context.applicationContext

        return instance ?: synchronized(this) {
            instance ?: buildApi(appContext).also { instance = it }
        }
    }

    private fun buildApi(appContext: Context): AguiaBrancaApi {
        val sessionManager = SessionManager(appContext)
        val authInterceptor = AuthInterceptor(sessionManager)

        // Adiciona log das requisições apenas no modo de desenvolvimento (Debug)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(AguiaBrancaApi::class.java)
    }
}