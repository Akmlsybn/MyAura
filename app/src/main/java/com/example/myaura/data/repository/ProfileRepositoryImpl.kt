package com.example.myaura.data.repository

import com.example.myaura.data.remote.ProfileRemoteDataSource
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.model.UserProfile
import com.example.myaura.domain.repository.ProfileRepository

class ProfileRepositoryImpl (
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override suspend fun saveUserProfile(uid: String, profile: UserProfile): Result<Unit> {
        return try {
            remoteDataSource.saveUserProfile(uid, profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(uid: String): Result<UserProfile?> {
        return try {
            val profile = remoteDataSource.getUserProfile(uid)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addPortfolio(userId: String, portfolioItem: PortfolioItem): Result<Unit> {
        return try {
            remoteDataSource.addPortfolio(userId, portfolioItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPortfolios(userId: String): Result<List<PortfolioItem>> {
        return try {
            val portfolios = remoteDataSource.getPortfolios(userId)
            Result.success(portfolios)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePortfolio(userId: String, portfolioId: String): Result<Unit> {
        return try {
            remoteDataSource.deletePortfolio(userId, portfolioId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPortfolio(userId: String, portfolioId: String): Result<PortfolioItem?> {
        return try {
            Result.success(remoteDataSource.getPortfolio(userId, portfolioId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePortfolio(userId: String, portfolioItem: PortfolioItem): Result<Unit> {
        return try {
            remoteDataSource.updatePortfolio(userId, portfolioItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addArticle(userId: String, article: Article): Result<Unit> {
        return try {
            remoteDataSource.addArticle(userId, article)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArticles(userId: String): Result<List<Article>> {
        return try {
            val articles = remoteDataSource.getArticles(userId)
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateArticle(userId: String, article: Article): Result<Unit> {
        return try {
            remoteDataSource.updateArticle(userId, article)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteArticle(userId: String, articleId: String): Result<Unit> {
        return try {
            remoteDataSource.deleteArticle(userId, articleId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArticle(userId: String, articleId: String): Result<Article?> {
        return try {
            val article = remoteDataSource.getArticle(userId, articleId)
            Result.success(article)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}