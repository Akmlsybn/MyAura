package com.example.myaura.domain.usecase

import com.example.myaura.domain.repository.ProfileRepository

class GetPortfoliosUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String) = repository.getPortfolios(userId)
}
