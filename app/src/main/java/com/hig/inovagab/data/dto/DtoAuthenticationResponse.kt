package com.hig.inovagab.data.dto

import com.hig.inovagab.model.User

data class DtoAuthenticationResponse(
    val token: String,
    val user: User
)
