package com.sobarna.newsapp.domain.model

data class News(
    val id: Int = 0,
    val publishedAt: String,
    val author: String,
    val urlToImage: String,
    val description: String,
    val source: String,
    val title: String,
    val url: String,
    val content: String
)
