package com.example.myaura.ui.profile.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.usecase.GetArticleUseCase
import com.example.myaura.domain.usecase.UpdateArticleUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditArticleViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val updateArticleUseCase: UpdateArticleUseCase,
    private val auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _editState = MutableStateFlow(EditArticleState())
    val editState = _editState.asStateFlow()

    private val articleId: String = savedStateHandle.get<String>("articleId")!!

    init {
        loadArticleDetails()
    }

    private fun loadArticleDetails() {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val uid = auth.currentUser?.uid ?: return@launch
            getArticleUseCase(uid, articleId)
                .onSuccess { article ->
                    if (article != null) {
                        _editState.value = _editState.value.copy(
                            isLoading = false,
                            title = article.title,
                            subTitle = article.subTitle,
                            content = article.content
                        )
                    } else {
                        _editState.value = _editState.value.copy(isLoading = false, error = "Artikel tidak ditemukan.")
                    }
                }
                .onFailure {
                    _editState.value = _editState.value.copy(isLoading = false, error = it.message)
                }
        }
    }

    fun onTitleChange(title: String) { _editState.value = _editState.value.copy(title = title) }
    fun onSubTitleChange(subTitle: String) { _editState.value = _editState.value.copy(subTitle = subTitle) }
    fun onContentChange(content: String) { _editState.value = _editState.value.copy(content = content) }

    fun onUpdateClicked() {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val uid = auth.currentUser?.uid ?: return@launch

            val currentState = _editState.value
            val updatedArticle = Article(
                id = articleId,
                userId = uid,
                title = currentState.title,
                subTitle = currentState.subTitle,
                content = currentState.content
            )

            updateArticleUseCase(uid, updatedArticle)
                .onSuccess {
                    _editState.value = _editState.value.copy(isSuccess = true, isLoading = false)
                }
                .onFailure {
                    _editState.value = _editState.value.copy(error = it.message, isLoading = false)
                }
        }
    }

    fun onNavigationDone() {
        _editState.value = _editState.value.copy(isSuccess = false, error = null)
    }
}

data class EditArticleState(
    val title: String = "",
    val subTitle: String = "",
    val content: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)