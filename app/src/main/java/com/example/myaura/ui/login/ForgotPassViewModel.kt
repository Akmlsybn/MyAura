package com.example.myaura.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.usecase.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ForgotPassState())
    val state = _state.asStateFlow()

    fun onSendLinkClicked(email: String) {
        viewModelScope.launch {
            _state.value = ForgotPassState(isLoading = true)
            forgotPasswordUseCase(email)
                .onSuccess { _state.value = ForgotPassState(isSuccess = true) }
                .onFailure { _state.value = ForgotPassState(error = it.message) }
        }
    }
}

data class ForgotPassState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)