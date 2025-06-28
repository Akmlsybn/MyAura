package com.example.myaura.domain.usecase

import com.example.myaura.domain.model.UserProfile
import com.example.myaura.domain.repository.ProfileRepository

class GetUserProfileUseCase(private val repository: ProfileRepository){
    suspend operator fun invoke(uid: String): Result<UserProfile?> {
        return repository.getUserProfile(uid)
    }
}