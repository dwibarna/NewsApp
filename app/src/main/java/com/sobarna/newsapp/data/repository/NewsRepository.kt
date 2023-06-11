package com.sobarna.newsapp.data.repository

import androidx.paging.PagingData
import com.sobarna.newsapp.data.Resource
import com.sobarna.newsapp.domain.model.News
import com.sobarna.newsapp.domain.model.Sources
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getAllSourcesFromCategory(category: String): Flow<Resource<List<Sources>>>

    fun getAllSourcesFromSearch(query: String): Flow<Resource<List<Sources>>>

    fun getAllNews(sources: String?, query: String?): Flow<PagingData<News>>

}