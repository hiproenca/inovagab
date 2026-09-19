package com.hig.inovagab.data.dto

import com.hig.inovagab.model.User

data class DtoAuthenticationRequest(
    val email: String,
    val password: String
)
