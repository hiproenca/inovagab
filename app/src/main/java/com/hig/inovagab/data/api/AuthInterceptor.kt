package com.hig.inovagab.data.api

import com.hig.inovagab.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (private val sessionManager: SessionManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking { sessionManager.jwtToken.first() }

        if(!token.isNullOrEmpty()){
            requestBuilder.addHeader("Authorization", "Bearer $token")

        }
        return chain.proceed(requestBuilder.build())
    }

}