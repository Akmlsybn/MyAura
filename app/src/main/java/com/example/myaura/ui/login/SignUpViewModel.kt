package com.example.myaura.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.local.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import com.example.myaura.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    fun onSignUpClicked(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _signUpState.value = SignUpState(error = "Email dan password tidak boleh kosong.")
            return
        }

        if (pass.length < 6) {
            _signUpState.value = SignUpState(error = "Password minimal harus 6 karakter.")
            return
        }

        viewModelScope.launch {
            _signUpState.value = SignUpState(isLoading = true)
            signUpUseCase(email, pass)
                .onSuccess {
                    sessionRepository.saveLoginSession(true)
                    _signUpState.value = SignUpState(isSuccess = true)
                }
                .onFailure { _signUpState.value = SignUpState(error = it.message) }
        }
    }
}

data class SignUpState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)