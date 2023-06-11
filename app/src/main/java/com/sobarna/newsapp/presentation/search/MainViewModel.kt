package com.sobarna.newsapp.presentation.search

import androidx.lifecycle.*
import com.sobarna.newsapp.domain.usecase.NewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: NewsUseCase) : ViewModel() {

    fun getAllSourcesFromCategory(category: String) = useCase.getAllSourcesFromCategory(category).asLiveData()

    fun getAllSourcesFromSearch(query: String) = useCase.getAllSourcesFromSearch(query).asLiveData()

    fun getAllNews(sources: String?, query: String?) = useCase.getAllNews(sources, query).asLiveData()

}