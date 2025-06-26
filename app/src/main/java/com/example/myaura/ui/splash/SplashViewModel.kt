package com.example.myaura.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _navigateEvent = MutableStateFlow<String?>(null)
    val navigateEvent = _navigateEvent.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000L)
            _navigateEvent.value = "onboarding"
        }
    }
}