package com.example.myaura.ui.profile.portfolio

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.usecase.GetPortfolioUseCase
import com.example.myaura.domain.usecase.UpdatePortfolioUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPortfolioViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val updatePortfolioUseCase: UpdatePortfolioUseCase,
    private val auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _editState = MutableStateFlow(EditPortfolioState())
    val editState = _editState.asStateFlow()

    private val portfolioId: String = savedStateHandle.get<String>("portfolioId")!!

    init {
        loadPortfolioDetails()
    }

    private fun loadPortfolioDetails() {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val uid = auth.currentUser?.uid ?: return@launch
            getPortfolioUseCase(uid, portfolioId)
                .onSuccess { portfolioItem ->
                    if (portfolioItem != null) {
                        _editState.value = _editState.value.copy(
                            isLoading = false,
                            title = portfolioItem.title,
                            description = portfolioItem.description,
                            startDate = portfolioItem.dateRange.split(" - ")[0],
                            endDate = portfolioItem.dateRange.split(" - ").getOrElse(1){ "" },
                            isCurrent = portfolioItem.dateRange.contains("Saat Ini"),
                            skill = portfolioItem.skill,
                            projectUrl = portfolioItem.projectUrl
                        )
                    } else {
                        _editState.value = _editState.value.copy(isLoading = false, error = "Portofolio tidak ditemukan.")
                    }
                }
                .onFailure {
                    _editState.value = _editState.value.copy(isLoading = false, error = it.message)
                }
        }
    }

    fun onTitleChange(title: String) { _editState.value = _editState.value.copy(title = title) }
    fun onDescriptionChange(desc: String) { _editState.value = _editState.value.copy(description = desc) }
    fun onSkillChange(skill: String) { _editState.value = _editState.value.copy(skill = skill) }
    fun onUrlChange(url: String) { _editState.value = _editState.value.copy(projectUrl = url) }

    fun onStartDateSelected(date: String) { _editState.value = _editState.value.copy(startDate = date) }
    fun onEndDateSelected(date: String) { _editState.value = _editState.value.copy(endDate = date) }
    fun onIsCurrentChange(isCurrent: Boolean) { _editState.value = _editState.value.copy(isCurrent = isCurrent) }


    fun onUpdateClicked() {
        viewModelScope.launch {
            _editState.value = _editState.value.copy(isLoading = true)
            val uid = auth.currentUser?.uid ?: return@launch

            val currentState = _editState.value
            val finalEndDate = if (currentState.isCurrent) "Sekarang" else currentState.endDate
            val dateRange = "${currentState.startDate} - $finalEndDate"

            val updatedPortfolio = PortfolioItem(
                id = portfolioId,
                userId = uid,
                title = currentState.title,
                description = currentState.description,
                dateRange = dateRange,
                skill = currentState.skill,
                projectUrl = currentState.projectUrl
            )

            updatePortfolioUseCase(uid, updatedPortfolio)
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

data class EditPortfolioState(
    val title: String = "",
    val description: String = "",
    val startDate: String = "Tanggal Mulai",
    val endDate: String = "Tanggal Selesai",
    val isCurrent: Boolean = false,
    val skill: String = "",
    val projectUrl: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)