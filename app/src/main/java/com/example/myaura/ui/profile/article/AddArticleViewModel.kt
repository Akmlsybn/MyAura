package com.example.myaura.ui.profile.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.usecase.AddArticleUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddArticleViewModel @Inject constructor(
    private val addArticleUseCase: AddArticleUseCase,
    private val auth: FirebaseAuth
) : ViewModel(){
    private val _addArticleState = MutableStateFlow(AddArticleState())
    val addArticleState = _addArticleState.asStateFlow()

    fun onPostArticleClicked(title: String, content: String, subTitle: String) {
        viewModelScope.launch {
            if (title.isBlank() || content.isBlank()) {
                _addArticleState.value = AddArticleState(error = "Judul dan konten tidak boleh kosong.")
                return@launch
            }

            _addArticleState.value = AddArticleState(isLoading = true)

            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid == null) {
                _addArticleState.value = AddArticleState(error = "Pengguna tidak ditemukan.")
                return@launch
            }

            val article = Article(userId = currentUserUid, title = title, subTitle = subTitle, content = content)

            addArticleUseCase(currentUserUid, article)
                .onSuccess {
                    _addArticleState.value = AddArticleState(isSuccess = true)
                }
                .onFailure {
                    _addArticleState.value = AddArticleState(error = it.message)
                }
        }
    }
    fun onNavigationDone() {
        _addArticleState.value = AddArticleState()
    }
}

data class AddArticleState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)