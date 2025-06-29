package com.example.myaura.ui.profile.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.remote.ProfileRemoteDataSource
import com.example.myaura.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ViewModel() {

    private val _articlesState = MutableStateFlow<ArticleListState>(ArticleListState.Loading)
    val articlesState = _articlesState.asStateFlow()

    init {
        fetchArticles()
    }

    fun fetchArticles() {
        viewModelScope.launch {
            _articlesState.value = ArticleListState.Loading
            try {
                val articles = profileRemoteDataSource.getAllArticles()
                _articlesState.value = ArticleListState.Success(articles)
            } catch (e: Exception) {
                _articlesState.value = ArticleListState.Error(e.message ?: "Failed to fetch articles")
            }
        }
    }
}

sealed interface ArticleListState {
    data object Loading : ArticleListState
    data class Success(val articles: List<Article>) : ArticleListState
    data class Error(val message: String) : ArticleListState
}