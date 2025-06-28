package com.example.myaura.domain.usecase

import com.example.myaura.domain.repository.ProfileRepository

class DeletePortfolioUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String, portfolioId: String) =
        repository.deletePortfolio(userId, portfolioId)
}