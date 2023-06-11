package com.sobarna.newsapp.di

import com.sobarna.newsapp.data.repository.NewsRepository
import com.sobarna.newsapp.data.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        DatabaseModule::class,
        NetworkModule::class
    ]
)
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repository: NewsRepositoryImpl): NewsRepository
}