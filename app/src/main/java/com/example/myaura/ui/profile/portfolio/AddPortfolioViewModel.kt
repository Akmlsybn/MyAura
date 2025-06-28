package com.example.myaura.ui.profile.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.usecase.AddPortfolioUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPortfolioViewModel @Inject constructor(
    private val addPortfolioUseCase: AddPortfolioUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _addState = MutableStateFlow(AddPortfolioState())
    val addState = _addState.asStateFlow()

    fun onAddPortfolioClicked(
        title: String,
        dateRange: String,
        skill: String,
        projectUrl: String,
        description: String
    ) {
        if (title.isBlank()) {
            _addState.value = AddPortfolioState(error = "Judul portofolio tidak boleh kosong.")
            return
        }
        if (dateRange.contains("Tanggal Mulai") || (dateRange.contains("Tanggal Selesai") && !dateRange.contains("Saat Ini"))) {
            _addState.value = AddPortfolioState(error = "Silakan lengkapi pilihan tanggal.")
            return
        }
        if (skill.isBlank()) {
            _addState.value = AddPortfolioState(error = "Skill tidak boleh kosong.")
            return
        }
        if (projectUrl.isBlank()) {
            _addState.value = AddPortfolioState(error = "URL project tidak boleh kosong.")
            return
        }
        if (description.isBlank()) {
            _addState.value = AddPortfolioState(error = "Deskripsi tidak boleh kosong.")
            return
        }

        viewModelScope.launch {
            _addState.value = AddPortfolioState(isLoading = true)

            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid == null) {
                _addState.value = AddPortfolioState(error = "Pengguna tidak ditemukan.")
                return@launch
            }

            val portfolioItem = PortfolioItem(
                userId = currentUserUid,
                title = title,
                dateRange = dateRange,
                skill = skill,
                projectUrl = projectUrl,
                description = description
            )
            addPortfolioUseCase(currentUserUid, portfolioItem)
                .onSuccess {
                    _addState.value = AddPortfolioState(isSuccess = true)
                }
                .onFailure {
                    _addState.value = AddPortfolioState(error = it.message)
                }
        }
    }
    fun onNavigationDone() {
        _addState.value = AddPortfolioState()
    }
}

data class AddPortfolioState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)