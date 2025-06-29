package com.example.myaura.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.local.SessionRepository
import com.example.myaura.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun onSignInClicked(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _signInState.value = SignInState(error = "Email dan password tidak boleh kosong.")
            return
        }
        viewModelScope.launch {
            _signInState.value = SignInState(isLoading = true)
            signInUseCase(email, pass)
                .onSuccess {
                    sessionRepository.saveLoginSession(true)
                    _signInState.value = SignInState(isSuccess = true)
                }
                .onFailure { _signInState.value = SignInState(error = it.message) }
        }
    }
}

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)