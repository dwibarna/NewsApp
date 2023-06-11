package com.sobarna.newsapp.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("news")
data class NewsEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo("publishedAt")
    val publishedAt: String?,

    @ColumnInfo("author")
    val author: String?,

    @ColumnInfo("urlToImage")
    val urlToImage: String?,

    @ColumnInfo("description")
    val description: String?,

    @ColumnInfo("source")
    val source: String?,

    @ColumnInfo("title")
    val title: String?,

    @ColumnInfo("url")
    val url: String?,

    @ColumnInfo("content")
    val content: String?
)