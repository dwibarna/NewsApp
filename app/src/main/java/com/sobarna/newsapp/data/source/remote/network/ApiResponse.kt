package com.sobarna.newsapp.data.source.remote.network

sealed class ApiResponse<out R> private constructor() {
    data class Success<out T>(val data: T): ApiResponse<T>()
    data class Error(val error: Throwable): ApiResponse<Nothing>()
}