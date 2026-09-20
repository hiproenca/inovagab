package com.hig.inovagab.data.repository

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


object SafeApiCall {
    suspend fun <T> execute (
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> T
    ): ApiResult<T> = withContext(dispatcher) {
        try {
            ApiResult.Success(apiCall())
        } catch (e: HttpException) {
            ApiResult.Error(httpMessage(e.code()))
        } catch (e: IOException) {
            ApiResult.Error("Sem conexão com o servidor. Verifique sua rede e tente novamente.")
        } catch (e: JsonDataException) {
            ApiResult.Error("Resposta inesperada do servidor.")
        }
    }

    private fun httpMessage(code: Int): String = when (code) {
        400 -> "Requisição inválida."
        401 -> "Sessão expirada ou não autorizada."
        403 -> "Você não tem permissão para esta ação."
        404 -> "Recurso não encontrado."
        409 -> "Conflito ao processar a requisição."
        in 500..599 -> "Erro no servidor. Tente novamente em instantes."
        else -> "Erro inesperado (HTTP $code)."
    }
}