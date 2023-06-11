package com.sobarna.newsapp.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sobarna.newsapp.data.source.local.entity.NewsEntity
import com.sobarna.newsapp.data.source.local.entity.RemoteKeys
import com.sobarna.newsapp.data.source.local.entity.SourceEntity

@Database(
    entities = [
        NewsEntity::class,
        RemoteKeys::class,
        SourceEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    abstract fun remoteDao(): RemoteKeysDao

    abstract fun sourceDao(): SourceDao
}