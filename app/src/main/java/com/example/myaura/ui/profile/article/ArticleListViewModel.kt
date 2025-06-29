package com.example.myaura.ui.profile.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.remote.ProfileRemoteDataSource
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.model.UserProfile
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

                val authorUids = articles.map { it.userId }.distinct()

                val authors = mutableMapOf<String, UserProfile>()
                for (uid in authorUids) {
                    val profile = profileRemoteDataSource.getUserProfile(uid)
                    if (profile != null) {
                        authors[uid] = profile
                    }
                }

                val articlesWithAuthors = articles.map { article ->
                    ArticleWithAuthor(
                        article = article,
                        author = authors[article.userId] ?: UserProfile()
                    )
                }

                _articlesState.value = ArticleListState.Success(articlesWithAuthors)
            } catch (e: Exception) {
                _articlesState.value = ArticleListState.Error(e.message ?: "Failed to fetch articles")
            }
        }
    }
}

data class ArticleWithAuthor(
    val article: Article,
    val author: UserProfile
)

sealed interface ArticleListState {
    data object Loading : ArticleListState
    data class Success(val articles: List<ArticleWithAuthor>) : ArticleListState
    data class Error(val message: String) : ArticleListState
}