package com.example.myaura.domain.repository

import com.example.myaura.domain.model.UserProfile

interface ProfileRepository {
    suspend fun saveUserProfile(uid: String, userProfile: UserProfile): Result<Unit>
    suspend fun getUserProfile(uid: String): Result<UserProfile?>
}