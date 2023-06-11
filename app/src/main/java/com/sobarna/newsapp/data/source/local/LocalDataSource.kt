package com.sobarna.newsapp.data.source.local

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.sobarna.newsapp.data.source.local.entity.NewsEntity
import com.sobarna.newsapp.data.source.local.entity.RemoteKeys
import com.sobarna.newsapp.data.source.local.entity.SourceEntity
import com.sobarna.newsapp.data.source.local.room.NewsDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocalDataSource @Inject constructor(private val database: NewsDatabase) {

    fun getAllNews(): PagingSource<Int, NewsEntity> = database.newsDao().getAllNews()

    suspend fun insertAllNews(newsEntities: List<NewsEntity>) =
        database.newsDao().insertAllNews(newsEntities)

    suspend fun insertAll(remoteKeys: List<RemoteKeys>) = database.remoteDao().insertAll(remoteKeys)

    suspend fun getRemoteKeysId(id: Int): RemoteKeys? = database.remoteDao().getRemoteKeysId(id)

    suspend fun dataBaseTransactions(state: Boolean) = database.withTransaction {
        if (state) {
            database.remoteDao().deleteRemoteKeys()
            database.newsDao().deleteAllNews()
        }
    }

    fun getAllSource(): Flow<List<SourceEntity>> =
        database.sourceDao().getAllSource()


    suspend fun insertAllSources(sourceEntities: List<SourceEntity>) =
        database.sourceDao().insertAllSources(sourceEntities)

    suspend fun deleteAllSources() =
        database.sourceDao().deleteAllSources()
}
