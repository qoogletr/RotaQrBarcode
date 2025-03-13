package com.rota.RotaQrBarcode.utils

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

    fun isSuccess() = this is Success
    fun isError() = this is Error
    fun isLoading() = this is Loading
}