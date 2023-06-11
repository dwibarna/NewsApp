package com.sobarna.newsapp.data.repository

import androidx.paging.*
import com.sobarna.newsapp.data.NetworkBoundRemoteMediator
import com.sobarna.newsapp.data.NetworkBoundResource
import com.sobarna.newsapp.data.Resource
import com.sobarna.newsapp.data.source.local.LocalDataSource
import com.sobarna.newsapp.data.source.local.entity.NewsEntity
import com.sobarna.newsapp.data.source.local.entity.RemoteKeys
import com.sobarna.newsapp.data.source.remote.RemoteDataSource
import com.sobarna.newsapp.data.source.remote.network.ApiResponse
import com.sobarna.newsapp.data.source.remote.response.NewsResponse
import com.sobarna.newsapp.data.source.remote.response.SourcesResponse
import com.sobarna.newsapp.domain.model.News
import com.sobarna.newsapp.domain.model.Sources
import com.sobarna.newsapp.util.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NewsRepository {
    override fun getAllSourcesFromCategory(category: String): Flow<Resource<List<Sources>>> {
        return object : NetworkBoundResource<List<Sources>, SourcesResponse>(){

            override fun loadFromDB(): Flow<List<Sources>> {
                return localDataSource.getAllSource().map {
                    DataMapper.mapSourceEntityToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<SourcesResponse>> {
                return remoteDataSource.getSourceFromCategory(category)
            }

            override suspend fun saveCallResult(data: SourcesResponse) {
                localDataSource.deleteAllSources()
                val source = DataMapper.mapSourceResponseToEntity(data)
                localDataSource.insertAllSources(source)
            }
        }.asFlow()
    }

    override fun getAllSourcesFromSearch(query: String): Flow<Resource<List<Sources>>> {
        return object : NetworkBoundResource<List<Sources>, NewsResponse>(){
            override fun loadFromDB(): Flow<List<Sources>> {
                return localDataSource.getAllSource().map {
                    DataMapper.mapSourceEntityToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<NewsResponse>> {
                return remoteDataSource.getSourceFromSearch(query)
            }

            override suspend fun saveCallResult(data: NewsResponse) {
                localDataSource.deleteAllSources()
                localDataSource.insertAllSources(
                    data.articles.let {
                        DataMapper.mapNewsResponseToSourceEntity(it)
                    }
                )
            }
        }.asFlow()
    }

    override fun getAllNews(sources: String?, query: String?): Flow<PagingData<News>> {
        return object : NetworkBoundRemoteMediator<NewsEntity, NewsResponse>() {
            override suspend fun fetchNetwork(page: Int): Flow<ApiResponse<NewsResponse>> {
               return if (query.isNullOrBlank() && sources.isNullOrBlank().not()){
                    remoteDataSource.getNewsFromSources(sources ?: "",page, PAGE_SIZE)
                } else
                    remoteDataSource.getNewsSearch(query, page, PAGE_SIZE)
            }

            override fun loadFromPagingDatabase(): PagingSource<Int, NewsEntity> {

             return localDataSource.getAllNews()
            }

            override fun stateEndOfPaginationReached(data: NewsResponse): Boolean {
                return data.articles?.isEmpty() == true
            }

            override suspend fun saveCallResult(
                data: NewsResponse,
                key: Pair<Int?, Int?>,
                loadType: LoadType
            ) {

                localDataSource.dataBaseTransactions(loadType == LoadType.REFRESH)
                localDataSource.insertAll(
                    data.articles?.map {
                        RemoteKeys(
                            prevKey = key.first,
                            nextKey = key.second
                        )
                    } ?: emptyList()
                )
                localDataSource.insertAllNews(
                    DataMapper.mapDomainToEntity(data)
                )
            }

            override suspend fun getRemoteKeyForClosestPosition(state: PagingState<Int, NewsEntity>): RemoteKeys? {
                return state.anchorPosition?.let {
                    state.closestItemToPosition(it)?.id?.let { id ->
                        localDataSource.getRemoteKeysId(id)
                    }
                }
            }

            override suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, NewsEntity>): RemoteKeys? {
                return state.pages.firstOrNull {
                    it.data.isNotEmpty()
                }?.data?.firstOrNull()?.let {
                    localDataSource.getRemoteKeysId(it.id)
                }
            }

            override suspend fun getRemoteKeyForLastItem(state: PagingState<Int, NewsEntity>): RemoteKeys? {
                return state.pages.lastOrNull {
                    it.data.isNotEmpty()
                }?.data?.lastOrNull()?.let {
                    localDataSource.getRemoteKeysId(it.id)
                }
            }

        }.asPaging(PAGE_SIZE)
            .map {
                it.map { news ->
                    DataMapper.mapEntityToDomain(news)
                }
            }

    }

    companion object {
        const val INITIAL_PAGE = 1
        const val PAGE_SIZE = 10
    }
}



