package com.example.myaura.domain.usecase

import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.repository.ProfileRepository

class AddPortfolioUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String, portfolioItem: PortfolioItem): Result<Unit> {
        if (portfolioItem.title.isBlank()) {
            return Result.failure(IllegalArgumentException("Judul portofolio tidak boleh kosong."))
        }
        return repository.addPortfolio(userId, portfolioItem)
    }
}