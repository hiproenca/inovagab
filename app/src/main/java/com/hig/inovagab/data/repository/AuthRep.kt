package com.hig.inovagab.data.repository

import com.hig.inovagab.data.dto.DtoAuthenticationRequest
import com.hig.inovagab.data.dto.DtoAuthenticationResponse
import com.hig.inovagab.data.dto.DtoRegisterRequest

interface AuthRep {
    suspend fun login(request: DtoAuthenticationRequest): ApiResult<DtoAuthenticationResponse>
    suspend fun register(request: DtoRegisterRequest): ApiResult<DtoAuthenticationResponse>
}