package com.example.myaura.ui.profile.portfolio

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.usecase.GetPortfolioUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioDetailViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _portfolioState = MutableStateFlow<PortfolioDetailState>(PortfolioDetailState.Loading)
    val portfolioState = _portfolioState.asStateFlow()

    private val portfolioId: String = savedStateHandle.get<String>("portfolioId")!!

    init {
        loadPortfolioDetails()
    }

    private fun loadPortfolioDetails() {
        viewModelScope.launch {
            _portfolioState.value = PortfolioDetailState.Loading
            val uid = auth.currentUser?.uid
            if (uid == null) {
                _portfolioState.value = PortfolioDetailState.Error("User tidak terautentikasi.")
                return@launch
            }

            getPortfolioUseCase(uid, portfolioId)
                .onSuccess { portfolioItem ->
                    _portfolioState.value = PortfolioDetailState.Success(portfolioItem)
                }
                .onFailure {
                    _portfolioState.value = PortfolioDetailState.Error(it.message ?: "Gagal memuat portofolio.")
                }
        }
    }
}

sealed interface PortfolioDetailState {
    data object Loading : PortfolioDetailState
    data class Success(val portfolioItem: PortfolioItem?) : PortfolioDetailState
    data class Error(val message: String) : PortfolioDetailState
}