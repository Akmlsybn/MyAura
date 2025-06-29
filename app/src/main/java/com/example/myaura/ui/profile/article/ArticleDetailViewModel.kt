package com.example.myaura.ui.profile.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.usecase.GetArticleUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _articleState = MutableStateFlow<ArticleDetailState>(ArticleDetailState.Loading)
    val articleState = _articleState.asStateFlow()

    private val articleId: String = savedStateHandle.get<String>("articleId")!!
    private val userId: String = savedStateHandle.get<String>("userId")!!

    init {
        loadArticleDetails()
    }

    private fun loadArticleDetails() {
        viewModelScope.launch {
            _articleState.value = ArticleDetailState.Loading

            getArticleUseCase(userId, articleId)
                .onSuccess { article ->
                    _articleState.value = ArticleDetailState.Success(article)
                }
                .onFailure {
                    _articleState.value = ArticleDetailState.Error(it.message ?: "Gagal memuat artikel.")
                }
        }
    }
}

sealed interface ArticleDetailState {
    data object Loading : ArticleDetailState
    data class Success(val article: Article?) : ArticleDetailState
    data class Error(val message: String) : ArticleDetailState
}