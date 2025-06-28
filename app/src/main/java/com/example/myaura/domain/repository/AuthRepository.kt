package com.example.myaura.domain.repository

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
}