package com.example.myaura.data.repository

import com.example.myaura.data.remote.AuthRemoteDataSource
import com.example.myaura.domain.repository.AuthRepository

class AuthRepositoryImpl (
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            remoteDataSource.signUp(email, password)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            remoteDataSource.signIn(email, password)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}

