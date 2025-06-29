package com.example.myaura.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.local.SessionRepository
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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getPortfoliosUseCase: GetPortfoliosUseCase,
    private val deletePortfolioUseCase: DeletePortfolioUseCase,
    private val getArticlesUseCase: GetArticlesUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val sessionRepository: SessionRepository,
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

            Log.d("ProfileViewModel", "Starting refreshData()... UID: $uid")
            val profileDeferred = async { getUserProfileUseCase(uid) }
            val portfoliosDeferred = async { getPortfoliosUseCase(uid) }
            val articlesDeferred = async { getArticlesUseCase(uid) }

            val profileResult = profileDeferred.await()
            val portfolioResult = portfoliosDeferred.await()
            val articleResult = articlesDeferred.await()

            Log.d("ProfileViewModel", "Profile fetch result: ${profileResult.isSuccess}")
            Log.d("ProfileViewModel", "Portfolios fetch result: ${portfolioResult.isSuccess} (count: ${portfolioResult.getOrNull()?.size})")
            Log.d("ProfileViewModel", "Articles fetch result: ${articleResult.isSuccess} (count: ${articleResult.getOrNull()?.size})")

            val userProfile = profileResult.getOrNull()
            val portfolios = portfolioResult.getOrNull()
            val articles = articleResult.getOrNull()

            if (profileResult.isSuccess && portfolioResult.isSuccess && articleResult.isSuccess) {
                _profileState.value = if (userProfile != null) {
                    Log.d("ProfileViewModel", "Refresh successful. Updating state.")
                    ProfileState.Success(userProfile, portfolios ?: emptyList(), articles ?: emptyList())
                } else {
                    Log.d("ProfileViewModel", "Refresh successful but profile is empty.")
                    ProfileState.Empty
                }
            } else {
                // Setidaknya satu panggilan API gagal
                val errorMessage = when {
                    profileResult.isFailure -> profileResult.exceptionOrNull()?.message ?: "Gagal Memuat Profil"
                    portfolioResult.isFailure -> portfolioResult.exceptionOrNull()?.message ?: "Gagal Memuat Portofolio"
                    articleResult.isFailure -> articleResult.exceptionOrNull()?.message ?: "Gagal Memuat Artikel"
                    else -> "Terjadi kesalahan saat memuat data."
                }
                Log.e("ProfileViewModel", "Refresh failed: $errorMessage")
                _profileState.value = ProfileState.Error(errorMessage)
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

    fun logout(){
        viewModelScope.launch {
            auth.signOut()
            sessionRepository.clearLoginSession()
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
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