package com.example.myaura.domain.usecase

import com.example.myaura.domain.model.Article
import com.example.myaura.domain.repository.ProfileRepository

class UpdateArticleUseCase (private val repository: ProfileRepository){
    suspend operator fun invoke(userId: String, article: Article) =
        repository.updateArticle(userId, article)
}