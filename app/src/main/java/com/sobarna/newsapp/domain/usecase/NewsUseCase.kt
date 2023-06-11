package com.sobarna.newsapp.domain.usecase

import androidx.paging.PagingData
import com.sobarna.newsapp.data.Resource
import com.sobarna.newsapp.domain.model.News
import com.sobarna.newsapp.domain.model.Sources
import kotlinx.coroutines.flow.Flow

interface NewsUseCase {

    fun getAllSourcesFromCategory(category: String): Flow<Resource<List<Sources>>>

    fun getAllSourcesFromSearch(query: String): Flow<Resource<List<Sources>>>

    fun getAllNews(sources: String?, query: String?): Flow<PagingData<News>>

}