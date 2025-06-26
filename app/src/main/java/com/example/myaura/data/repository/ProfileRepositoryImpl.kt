package com.example.myaura.data.repository

import com.example.myaura.data.remote.ProfileRemoteDataSource
import com.example.myaura.domain.model.UserProfile
import com.example.myaura.domain.repository.ProfileRepository

class ProfileRepositoryImpl (
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override suspend fun saveUserProfile(uid: String, profile: UserProfile): Result<Unit> {
        return try {
            remoteDataSource.saveUserProfile(uid, profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(uid: String): Result<UserProfile?> {
        return try {
            val profile = remoteDataSource.getUserProfile(uid)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}