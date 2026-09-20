package com.hig.inovagab.data.repository

import com.hig.inovagab.data.api.AguiaBrancaApi
import com.hig.inovagab.data.dto.DtoAuthenticationRequest
import com.hig.inovagab.data.dto.DtoAuthenticationResponse
import com.hig.inovagab.data.dto.DtoRegisterRequest
import com.hig.inovagab.data.local.SessionManager
import com.hig.inovagab.data.repository.SafeApiCall.execute
import com.squareup.moshi.JsonDataException

class AuthRepImpl(private val api : AguiaBrancaApi, private val sessionManager: SessionManager): AuthRep {
    override suspend fun login(request: DtoAuthenticationRequest): ApiResult<DtoAuthenticationResponse> =
        execute {
            api.login(request).also { response ->
                val userId = response.user.id
                    ?: throw JsonDataException("Login sem id de usuário")
                sessionManager.saveSession(
                    token = response.token,
                    role = response.user.role.name,
                    userId = userId
                )
            }
        }

    override suspend fun register(request: DtoRegisterRequest): ApiResult<DtoAuthenticationResponse> =
        execute { api.register(request) }
}