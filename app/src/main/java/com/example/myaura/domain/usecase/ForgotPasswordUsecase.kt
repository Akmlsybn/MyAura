package com.example.myaura.domain.usecase

import com.example.myaura.domain.repository.AuthRepository

class ForgotPasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email tidak boleh kosong."))
        }
        return repository.sendPasswordReset(email)
    }
}