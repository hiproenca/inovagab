package com.hig.inovagab.data.api

import com.hig.inovagab.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (private val sessionManager: SessionManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if(!request.url.toString().contains("/api/auth/")){
            val token = runBlocking {sessionManager.jwtToken.first()}

            if(!token.isNullOrEmpty()){
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }
        val response = chain.proceed(requestBuilder.build())

        if(response.code == 401){runBlocking { sessionManager.clearSession() }}
        return response
    }

}