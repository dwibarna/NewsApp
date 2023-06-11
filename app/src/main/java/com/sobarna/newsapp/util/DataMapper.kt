package com.sobarna.newsapp.util

import com.sobarna.newsapp.data.source.local.entity.NewsEntity
import com.sobarna.newsapp.data.source.local.entity.SourceEntity
import com.sobarna.newsapp.data.source.remote.response.ArticlesItem
import com.sobarna.newsapp.data.source.remote.response.NewsResponse
import com.sobarna.newsapp.data.source.remote.response.SourcesResponse
import com.sobarna.newsapp.domain.model.News
import com.sobarna.newsapp.domain.model.Sources

object DataMapper {
    fun mapEntityToDomain(newsEntity: NewsEntity): News {
        with(newsEntity){
            return News(
                id = id,
                publishedAt = publishedAt ?: "",
                author = author ?: "",
                urlToImage = urlToImage ?: "",
                description = description ?: "",
                source = source ?: "",
                title = title ?: "",
                url = url.toString(),
                content = content.toString()
            )
        }
    }

    fun mapDomainToEntity(newsResponse: NewsResponse): List<NewsEntity> {
        val list:MutableList<NewsEntity> = mutableListOf()

        newsResponse.articles?.forEach {
            NewsEntity(
                id = 0,
                publishedAt = it.publishedAt ?: "",
                author = it.author ?: "",
                urlToImage = it.urlToImage ?: "",
                description = it.description ?: "",
                source = it.source?.name ?: "",
                title = it.title ?: "",
                url = it.url.toString(),
                content = it.content.toString()
            ).let(list::add)
        }
        return list
    }

    fun mapSourceEntityToDomain(entity: List<SourceEntity>): List<Sources> {
        val list = mutableListOf<Sources>()

        entity.forEach {
            Sources(
                id = it.id,
                name = it.source,
                idSource = it.idSource
            ).let(list::add)
        }
        return list
    }

    fun mapSourceResponseToEntity(sourcesResponse: SourcesResponse): List<SourceEntity> {
        val list = mutableListOf<SourceEntity>()

        sourcesResponse.sources?.forEach {
            SourceEntity(
                id = 0,
                source = it?.name ?: "",
                idSource = it?.id ?: ""
            ).let (list::add)
        }
        return list
    }

    fun mapNewsResponseToSourceEntity(articlesItems: List<ArticlesItem>?): List<SourceEntity> {
        val list = mutableListOf<SourceEntity>()

        articlesItems?.forEach {
            SourceEntity(
                id = 0,
                source = it.source?.name ?: "",
                idSource = it.source?.id ?: ""
            ).let(list::add)
        }
        return list
    }
}