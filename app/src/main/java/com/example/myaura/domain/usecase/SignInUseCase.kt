package com.example.myaura.domain.usecase

import com.example.myaura.domain.repository.AuthRepository

class SignInUseCase (private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email and password cannot be empty"))
        }
        return repository.signIn(email, password)
    }
}