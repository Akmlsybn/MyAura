package com.example.myaura.domain.usecase

import com.example.myaura.domain.repository.ProfileRepository

class GetArticlesUseCase (private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String) = repository.getArticles(userId)
}