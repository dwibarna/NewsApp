package com.sobarna.newsapp.presentation.news

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.sobarna.newsapp.domain.model.News
import com.sobarna.newsapp.domain.usecase.NewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val useCase: NewsUseCase) : ViewModel() {

    private val _onLoading = MutableLiveData<Boolean>()
    val onLoading: LiveData<Boolean>
        get() = _onLoading

    private val _newsLiveData = MutableLiveData<PagingData<News>>()
    val newsLiveData: LiveData<PagingData<News>>
        get() = _newsLiveData

    fun getAllNews(query: String?, source: String?) = viewModelScope.launch {
        _onLoading.value = true
        useCase.getAllNews(source, query).collectLatest { paging ->
            _onLoading.value = false
            _newsLiveData.value = paging
        }
    }
}