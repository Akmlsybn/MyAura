package com.example.myaura.data.repository

import com.example.myaura.data.local.dao.ArticleDao
import com.example.myaura.data.local.dao.PortfolioDao
import com.example.myaura.data.local.dao.UserProfileDao
import com.example.myaura.data.local.mapper.toDomain
import com.example.myaura.data.local.mapper.toEntity
import com.example.myaura.data.remote.ProfileRemoteDataSource
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.model.UserProfile
import com.example.myaura.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.first

class ProfileRepositoryImpl (
    private val remoteDataSource: ProfileRemoteDataSource,
    private val userProfileDao: UserProfileDao,
    private val portfolioDao: PortfolioDao,
    private val articleDao: ArticleDao
) : ProfileRepository {
    override suspend fun saveUserProfile(uid: String, profile: UserProfile): Result<Unit> {
        return try {
            remoteDataSource.saveUserProfile(uid, profile)
            userProfileDao.insertUserProfile(profile.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(uid: String): Result<UserProfile?> {
        return try {
            val profile = remoteDataSource.getUserProfile(uid)
            if (profile != null) {
                userProfileDao.insertUserProfile(profile.toEntity())
            }
            Result.success(profile)
        } catch (e: Exception) {
            val cachedProfile = userProfileDao.getUserProfile(uid).first()
            Result.success(cachedProfile?.toDomain())
        }
    }

    override suspend fun addPortfolio(userId: String, portfolioItem: PortfolioItem): Result<Unit> {
        return try {
            remoteDataSource.addPortfolio(userId, portfolioItem)
            portfolioDao.insertPortfolio(portfolioItem.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPortfolios(userId: String): Result<List<PortfolioItem>> {
        return try {
            val portfolios = remoteDataSource.getPortfolios(userId)
            portfolioDao.insertPortfolios(portfolios.map { it.toEntity() })
            Result.success(portfolios)
        } catch (e: Exception) {
            val cachedPortfolios = portfolioDao.getPortfoliosByUserId(userId).first()
            Result.success(cachedPortfolios.map { it.toDomain() })
        }
    }

    override suspend fun deletePortfolio(userId: String, portfolioId: String): Result<Unit> {
        return try {
            remoteDataSource.deletePortfolio(userId, portfolioId)
            portfolioDao.deletePortfolioById(userId, portfolioId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPortfolio(userId: String, portfolioId: String): Result<PortfolioItem?> {
        return try {
            val remotePortfolio = remoteDataSource.getPortfolio(userId, portfolioId)
            if (remotePortfolio != null) {
                portfolioDao.insertPortfolio(remotePortfolio.toEntity())
            }
            Result.success(remotePortfolio)
        } catch (e: Exception) {
            val cachedPortfolio = portfolioDao.getPortfolioById(userId, portfolioId)
            Result.success(cachedPortfolio?.toDomain())
        }
    }

    override suspend fun updatePortfolio(userId: String, portfolioItem: PortfolioItem): Result<Unit> {
        return try {
            remoteDataSource.updatePortfolio(userId, portfolioItem)
            portfolioDao.updatePortfolio(portfolioItem.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addArticle(userId: String, article: Article): Result<Unit> {
        return try {
            remoteDataSource.addArticle(userId, article)
            articleDao.insertArticles(listOf(article.toEntity()))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArticles(userId: String): Result<List<Article>> {
        return try {
            val articles = remoteDataSource.getArticles(userId)
            articleDao.insertArticles(articles.map { it.toEntity() })
            Result.success(articles)
        } catch (e: Exception) {
            val cachedArticles = articleDao.getArticlesByUserId(userId).first()
            Result.success(cachedArticles.map { it.toDomain() })
        }
    }

    override suspend fun updateArticle(userId: String, article: Article): Result<Unit> {
        return try {
            remoteDataSource.updateArticle(userId, article)
            articleDao.insertArticles(listOf(article.toEntity()))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteArticle(userId: String, articleId: String): Result<Unit> {
        return try {
            remoteDataSource.deleteArticle(userId, articleId)
            articleDao.deleteArticleById(articleId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArticle(userId: String, articleId: String): Result<Article?> {
        return try {
            val remoteArticle = remoteDataSource.getArticle(userId, articleId)
            if (remoteArticle != null) {
                articleDao.insertArticles(listOf(remoteArticle.toEntity()))
            }
            Result.success(remoteArticle)
        } catch (e: Exception) {
            val cachedArticle = articleDao.getArticleById(articleId)
            Result.success(cachedArticle?.toDomain())
        }
    }
}