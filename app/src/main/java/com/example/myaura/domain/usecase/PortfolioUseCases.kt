package com.example.myaura.domain.usecase

import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.repository.ProfileRepository

class GetPortfolioUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String, portfolioId: String) =
        repository.getPortfolio(userId, portfolioId)
}

class UpdatePortfolioUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String, portfolioItem: PortfolioItem) =
        repository.updatePortfolio(userId, portfolioItem)
}