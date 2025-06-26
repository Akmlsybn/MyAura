package com.example.myaura.domain.usecase

import com.example.myaura.domain.model.UserProfile
import com.example.myaura.domain.repository.ProfileRepository

class SaveUserProfileUseCase (private val repository: ProfileRepository) {
    suspend operator fun invoke(uid: String, profile: UserProfile) =
        repository.saveUserProfile(uid, profile)
}