package com.hig.inovagab.data.repository

sealed class ApiResult <out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: String) : ApiResult<Nothing>()
}
