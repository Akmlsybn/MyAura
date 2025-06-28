package com.example.myaura.domain.usecase

import com.example.myaura.domain.repository.ProfileRepository

class GetArticleUseCase (private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String, articleId: String) =
        repository.getArticle(userId, articleId)
}