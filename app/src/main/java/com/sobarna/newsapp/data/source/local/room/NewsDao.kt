package com.sobarna.newsapp.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sobarna.newsapp.data.source.local.entity.NewsEntity


@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getAllNews(): PagingSource<Int, NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(gamesEntity: List<NewsEntity>)

    @Query("DELETE from news")
    suspend fun deleteAllNews()

}