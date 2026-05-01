package org.example.tugas6pamra.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.tugas6pamra.data.Article
import org.example.tugas6pamra.data.NewsRepository

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _currentCategory = MutableStateFlow<String?>(null)
    val currentCategory: StateFlow<String?> = _currentCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // Fitur Bookmark
    private val _bookmarks = MutableStateFlow<Set<String>>(emptySet())
    val bookmarks: StateFlow<Set<String>> = _bookmarks.asStateFlow()

    val categories = listOf("Business", "Technology", "Entertainment", "Health", "Science", "Sports")

    init {
        fetchNews()
    }

    fun fetchNews(category: String? = _currentCategory.value, query: String? = _searchQuery.value) {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            _currentCategory.value = category
            try {
                val articles = repository.getNews(category = category?.lowercase(), query = query)
                if (articles.isEmpty()) {
                    _uiState.value = NewsUiState.Error("No articles found.")
                } else {
                    _uiState.value = NewsUiState.Success(articles)
                }
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "Failed to load news.")
            }
        }
    }

    fun toggleBookmark(articleUrl: String) {
        val current = _bookmarks.value.toMutableSet()
        if (current.contains(articleUrl)) current.remove(articleUrl) else current.add(articleUrl)
        _bookmarks.value = current
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
