package org.example.tugas6pamra.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Repository for fetching news articles from NewsAPI.
 */
class NewsRepository {
    private val apiKey = "e318b8beda7e4a4ab8c6c3bf96424540"
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    /**
     * Fetches top headlines from NewsAPI with optional category and query.
     */
    suspend fun getNews(category: String? = null, query: String? = null): List<Article> {
        val response: NewsResponse = client.get("https://newsapi.org/v2/top-headlines") {
            parameter("country", "us")
            parameter("apiKey", apiKey)
            if (!category.isNullOrEmpty()) {
                parameter("category", category)
            }
            if (!query.isNullOrEmpty()) {
                parameter("q", query)
            }
            header(HttpHeaders.UserAgent, "ComposeNewsReader/1.0")
        }.body()
        
        if (response.status != "ok") {
            throw Exception("Failed to fetch news: ${response.status}")
        }
        
        // Filter out articles with removed content or missing vital info
        return response.articles.filter { it.title != "[Removed]" && it.title.isNotEmpty() }
    }
}
