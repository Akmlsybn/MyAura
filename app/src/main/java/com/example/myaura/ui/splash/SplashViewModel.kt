package com.example.myaura.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.data.local.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {
    private val _navigateEvent = MutableStateFlow<String?>(null)
    val navigateEvent = _navigateEvent.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000L)
            val isLoggedIn = sessionRepository.isLoggedIn.first()
            if (isLoggedIn) {
                _navigateEvent.value = "profile_page"
            } else {
                _navigateEvent.value = "onboarding"
            }
        }
    }
}