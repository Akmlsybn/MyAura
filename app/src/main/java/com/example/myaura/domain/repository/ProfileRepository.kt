package com.example.myaura.domain.repository

import com.example.myaura.domain.model.Article
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.model.UserProfile

interface ProfileRepository {
    suspend fun saveUserProfile(uid: String, userProfile: UserProfile): Result<Unit>
    suspend fun getUserProfile(uid: String): Result<UserProfile?>

    suspend fun addPortfolio(userId: String, portfolioItem: PortfolioItem): Result<Unit>
    suspend fun getPortfolios(userId: String): Result<List<PortfolioItem>>
    suspend fun deletePortfolio(userId: String, portfolioId: String): Result<Unit>
    suspend fun updatePortfolio(userId: String, portfolioItem: PortfolioItem): Result<Unit>
    suspend fun getPortfolio(userId: String, portfolioId: String): Result<PortfolioItem?>

    suspend fun addArticle(userId: String, article: Article): Result<Unit>
    suspend fun getArticles(userId: String): Result<List<Article>>
    suspend fun updateArticle(userId: String, article: Article): Result<Unit>
    suspend fun deleteArticle(userId: String, articleId: String): Result<Unit>
    suspend fun getArticle(userId: String, articleId: String): Result<Article?>

}