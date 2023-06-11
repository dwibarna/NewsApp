package com.sobarna.newsapp.di

import android.content.Context
import androidx.room.Room
import com.sobarna.newsapp.data.source.local.room.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(
            context = context,
            klass = NewsDatabase::class.java,
            name = "news.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideNewsDao(database: NewsDatabase) = database.newsDao()

    @Provides
    fun provideRemoteDao(database: NewsDatabase) = database.remoteDao()

    @Provides
    fun provideSourceDao(database: NewsDatabase) = database.sourceDao()
}