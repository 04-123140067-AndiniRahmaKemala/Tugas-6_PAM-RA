package org.example.tugas6pamra.data

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int? = null,
    val articles: List<Article> = emptyList()
)

@Serializable
data class Article(
    val title: String = "",
    val description: String? = null,
    val url: String = "",
    val urlToImage: String? = null,
    val publishedAt: String = "",
    val content: String? = null,
    val source: Source = Source()
)

@Serializable
data class Source(
    val id: String? = null,
    val name: String = ""
)
