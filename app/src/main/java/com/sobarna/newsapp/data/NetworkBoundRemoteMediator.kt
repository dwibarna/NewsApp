package com.sobarna.newsapp.data

import androidx.paging.*
import com.sobarna.newsapp.data.repository.NewsRepositoryImpl.Companion.INITIAL_PAGE
import com.sobarna.newsapp.data.source.local.entity.RemoteKeys
import com.sobarna.newsapp.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalPagingApi::class)
abstract class NetworkBoundRemoteMediator<ResultType : Any, RequestType : Any> {

    fun asPaging(pageSize: Int): Flow<PagingData<ResultType>> = flow {
        emitAll(
            Pager(
                config = PagingConfig(
                    pageSize = pageSize,
                ),
                remoteMediator = createRemoteMediator(),
                pagingSourceFactory = {
                    loadFromPagingDatabase()
                }
            ).flow
        )
    }

    protected abstract suspend fun fetchNetwork(page: Int): Flow<ApiResponse<RequestType>>

    private fun createRemoteMediator(): RemoteMediator<Int, ResultType> =
        object : RemoteMediator<Int, ResultType>() {

            override suspend fun initialize(): InitializeAction {
                return super.initialize()
            }

            override suspend fun load(
                loadType: LoadType,
                state: PagingState<Int, ResultType>
            ): MediatorResult {
                try {
                    val remoteKeys: RemoteKeys?
                    val page = when (loadType) {
                        LoadType.REFRESH -> {
                            remoteKeys = getRemoteKeyForClosestPosition(state)
                            remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE
                        }
                        LoadType.PREPEND -> {
                            remoteKeys = getRemoteKeyForFirstItem(state)
                            val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                                endOfPaginationReached = remoteKeys != null
                            )
                            prevKey
                        }
                        LoadType.APPEND -> {
                            remoteKeys = getRemoteKeyForLastItem(state)
                            val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                                endOfPaginationReached = remoteKeys != null
                            )
                            nextKey
                        }
                    }



                    when (val response = fetchNetwork(page).single()) {
                        is ApiResponse.Error -> {
                            return MediatorResult.Error(response.error)
                        }
                        is ApiResponse.Success -> {
                            val prevKey = if (page == 1) null else page - 1
                            val nextKey =
                                if (stateEndOfPaginationReached(response.data))
                                    null
                                else
                                    page + 1

                            saveCallResult(response.data, Pair(prevKey, nextKey), loadType)
                            return MediatorResult.Success(endOfPaginationReached = nextKey == null)
                        }
                    }

                } catch (e: Throwable) {
                    return MediatorResult.Error(e)
                }
            }
        }

    abstract suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ResultType>): RemoteKeys?

    abstract suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ResultType>): RemoteKeys?

    abstract suspend fun getRemoteKeyForClosestPosition(state: PagingState<Int, ResultType>): RemoteKeys?

    protected abstract suspend fun saveCallResult(
        data: RequestType,
        key: Pair<Int?, Int?>,
        loadType: LoadType
    )

    protected abstract fun stateEndOfPaginationReached(data: RequestType): Boolean

    protected abstract fun loadFromPagingDatabase(): PagingSource<Int, ResultType>
}

