package com.sobarna.newsapp.di

import com.sobarna.newsapp.domain.usecase.NewsUseCase
import com.sobarna.newsapp.domain.usecase.NewsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideNewsUseCase(useCaseImpl: NewsUseCaseImpl): NewsUseCase
}