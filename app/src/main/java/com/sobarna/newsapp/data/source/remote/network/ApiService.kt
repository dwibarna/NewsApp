package com.sobarna.newsapp.data.source.remote.network

import com.sobarna.newsapp.BuildConfig.BASE_API_KEY
import com.sobarna.newsapp.data.source.remote.response.NewsResponse
import com.sobarna.newsapp.data.source.remote.response.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v2/top-headlines/sources")
    suspend fun getSourceFromCategory(
        @Query("apiKey") apiKey: String = BASE_API_KEY,
        @Query("category") category: String? = null
    ): SourcesResponse

    @GET("/v2/top-headlines")
    suspend fun getNewsFromSources(
        @Query("apiKey") apiKey: String = BASE_API_KEY,
        @Query("sources") sources: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): NewsResponse

    @GET("/v2/everything")
    suspend fun getSourceFromSearch(
        @Query("apiKey") apiKey: String = BASE_API_KEY,
        @Query("sources") sources: String,
        @Query("sortBy") sortBy: String = "relevancy"
    ): NewsResponse

    @GET("/v2/everything")
    suspend fun getNewsSearch(
        @Query("apiKey") apiKey: String = BASE_API_KEY,
        @Query("q") query: String? = null,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("searchIn") typeSearch: String = "title,content",
        @Query("sortBy") sortType: String = "relevancy"
    ): NewsResponse
}