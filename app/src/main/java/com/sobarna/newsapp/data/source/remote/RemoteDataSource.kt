package com.sobarna.newsapp.data.source.remote

import android.util.Log
import com.sobarna.newsapp.data.source.remote.network.ApiResponse
import com.sobarna.newsapp.data.source.remote.network.ApiService
import com.sobarna.newsapp.data.source.remote.response.NewsResponse
import com.sobarna.newsapp.data.source.remote.response.SourcesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getSourceFromCategory(category: String?): Flow<ApiResponse<SourcesResponse>> = flow {
        try {
            emit(ApiResponse.Success(apiService.getSourceFromCategory(category = category)))
        } catch (e: Throwable) {
            emit(ApiResponse.Error(e))
            Log.e(javaClass.name, e.message.toString())
        }
    }.flowOn(Dispatchers.IO)


    suspend fun getNewsFromSources(sources: String, page: Int, pageSize: Int): Flow<ApiResponse<NewsResponse>> = flow {
        try {
            emit(ApiResponse.Success(apiService.getNewsFromSources(sources = sources, page = page, pageSize = pageSize)))
        } catch (e: Throwable) {
            emit(ApiResponse.Error(e))
            Log.e(javaClass.name, e.message.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getSourceFromSearch(query: String): Flow<ApiResponse<NewsResponse>> = flow {
        try {
            emit(ApiResponse.Success(apiService.getSourceFromSearch(sources = query)))
        } catch (e: Throwable) {
            emit(ApiResponse.Error(e))
            Log.e(javaClass.name, e.message.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getNewsSearch(query: String?, page: Int, pageSize: Int): Flow<ApiResponse<NewsResponse>> = flow {
        try {
            emit(ApiResponse.Success(apiService.getNewsSearch(query = query, page = page, pageSize = pageSize)))
        } catch (e: Throwable) {
            emit(ApiResponse.Error(e))
            Log.e(javaClass.name, e.message.toString())
        }
    }.flowOn(Dispatchers.IO)

}