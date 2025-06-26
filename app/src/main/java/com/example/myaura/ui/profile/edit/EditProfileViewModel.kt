package com.example.myaura.ui.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaura.domain.model.UserProfile
import com.example.myaura.domain.usecase.SaveUserProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _saveState = MutableStateFlow(EditProfileState())
    val saveState = _saveState.asStateFlow()

    fun onSaveClicked(name: String, job: String, tagline: String, bio: String, socialLinks: Map<String, String>) {
        viewModelScope.launch {
            _saveState.value = EditProfileState(isLoading = true)

            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid == null) {
                _saveState.value = EditProfileState(error = "User not authenticated")
                return@launch
            }
            val userProfile = UserProfile(
                uid = currentUserUid,
                name = name,
                job = job,
                tagline = tagline,
                bio = bio,
                socialMediaLinks = socialLinks
            )
            saveUserProfileUseCase(currentUserUid, userProfile).onSuccess {
                _saveState.value = EditProfileState(isSuccess = true)
            }.onFailure {
                _saveState.value = EditProfileState(error = it.message ?: "Unknown error")
            }
        }
    }
    fun onNavigationDone() {
        _saveState.value = EditProfileState(isLoading = false, isSuccess = false, error = null)
    }
}

data class EditProfileState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)