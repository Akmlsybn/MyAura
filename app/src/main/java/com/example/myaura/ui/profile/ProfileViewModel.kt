package com.example.myaura.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.model.UserProfile
import com.example.myaura.domain.usecase.DeleteArticleUseCase
import com.example.myaura.domain.usecase.GetArticlesUseCase
import com.example.myaura.domain.usecase.DeletePortfolioUseCase
import com.example.myaura.domain.usecase.GetPortfoliosUseCase
import com.example.myaura.domain.usecase.GetUserProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // Profil
    private val getUserProfileUseCase: GetUserProfileUseCase,
    // Portofolio
    private val getPortfoliosUseCase: GetPortfoliosUseCase,
    private val deletePortfolioUseCase: DeletePortfolioUseCase,
    // **PERUBAHAN 1: Tambahkan UseCase untuk Artikel**
    private val getArticlesUseCase: GetArticlesUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState = _profileState.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            val uid = auth.currentUser?.uid
            if (uid == null) {
                _profileState.value = ProfileState.Error("User not authenticated")
                return@launch
            }

            // **PERUBAHAN 2: Ambil data artikel juga**
            val profileResult = getUserProfileUseCase(uid)
            val portfolioResult = getPortfoliosUseCase(uid)
            val articleResult = getArticlesUseCase(uid)

            profileResult.onSuccess { userProfile ->
                portfolioResult.onSuccess { portfolios ->
                    articleResult.onSuccess { articles ->
                        _profileState.value = if (userProfile != null) {
                            ProfileState.Success(userProfile, portfolios, articles)
                        } else {
                            ProfileState.Empty
                        }
                    }.onFailure {
                        _profileState.value = ProfileState.Error(it.message ?: "Gagal Memuat Artikel")
                    }
                }.onFailure {
                    _profileState.value = ProfileState.Error(it.message ?: "Gagal Memuat Portofolio")
                }
            }.onFailure {
                _profileState.value = ProfileState.Error(it.message ?: "Gagal Memuat Profil")
            }
        }
    }

    fun deletePortfolio(portfolioId: String) {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            deletePortfolioUseCase(uid, portfolioId)
                .onSuccess { refreshData() }
                .onFailure { _profileState.value = ProfileState.Error(it.message ?: "Gagal Menghapus Portofolio") }
        }
    }

    fun deleteArticle(articleId: String) {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            deleteArticleUseCase(uid, articleId)
                .onSuccess { refreshData() }
                .onFailure { _profileState.value = ProfileState.Error(it.message ?: "Gagal Menghapus Artikel") }
        }
    }
}

sealed interface ProfileState {
    data object Loading : ProfileState
    data object Empty : ProfileState
    data class Success(
        val userProfile: UserProfile,
        val portfolios: List<PortfolioItem>,
        val articles: List<Article>
    ) : ProfileState
    data class Error(val message: String) : ProfileState
}