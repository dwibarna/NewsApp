package com.sobarna.newsapp.domain.usecase

import androidx.paging.PagingData
import com.sobarna.newsapp.data.Resource
import com.sobarna.newsapp.data.repository.NewsRepository
import com.sobarna.newsapp.domain.model.News
import com.sobarna.newsapp.domain.model.Sources
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsUseCaseImpl @Inject constructor(private val repository: NewsRepository): NewsUseCase {

    override fun getAllSourcesFromCategory(category: String): Flow<Resource<List<Sources>>> {
        return repository.getAllSourcesFromCategory(category)
    }

    override fun getAllSourcesFromSearch(query: String): Flow<Resource<List<Sources>>> {
        return repository.getAllSourcesFromSearch(query)
    }

    override fun getAllNews(sources: String?, query: String?): Flow<PagingData<News>> {
        return repository.getAllNews(sources, query)
    }
}