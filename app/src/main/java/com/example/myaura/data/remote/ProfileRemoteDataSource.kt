package com.example.myaura.data.remote

import com.example.myaura.domain.model.Article
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source

import kotlinx.coroutines.tasks.await

class ProfileRemoteDataSource (private val firestore: FirebaseFirestore) {
    suspend fun saveUserProfile(uid: String, profile: UserProfile) {
        firestore.collection("users").document(uid).set(profile).await()
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return firestore.collection("users").document(uid).get(Source.SERVER).await()
            .toObject(UserProfile::class.java)
    }

    suspend fun addPortfolio(userId: String, portfolioItem: PortfolioItem) {
        val newPortfolioRef = firestore.collection("users").document(userId)
            .collection("portfolios").document()
        val portfolioWithId = portfolioItem.copy(id = newPortfolioRef.id)
        newPortfolioRef.set(portfolioWithId).await()
    }

    suspend fun getPortfolios(userId: String): List<PortfolioItem>{
        return firestore.collection("users").document(userId)
            .collection("portfolios")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get(Source.SERVER).await().toObjects(PortfolioItem::class.java)
    }

    suspend fun deletePortfolio(userId: String, portfolioId: String) {
        firestore.collection("users").document(userId)
            .collection("portfolios").document(portfolioId).delete().await()
    }

    suspend fun getPortfolio(userId: String, portfolioId: String): PortfolioItem? {
        return firestore.collection("users").document(userId)
            .collection("portfolios").document(portfolioId)
            .get(Source.SERVER).await().toObject(PortfolioItem::class.java)
    }

    suspend fun updatePortfolio(userId: String, portfolioItem: PortfolioItem) {
        firestore.collection("users").document(userId)
            .collection("portfolios").document(portfolioItem.id)
            .set(portfolioItem).await()
    }

    suspend fun addArticle(userId: String, article: Article) {
        val newArticleRef = firestore.collection("users").document(userId)
            .collection("articles").document()
        val articleWithId = article.copy(id = newArticleRef.id)
        newArticleRef.set(articleWithId).await()
    }

    suspend fun getArticles(userId: String): List<Article> {
        return firestore.collection("users").document(userId)
            .collection("articles")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get(Source.SERVER).await().toObjects(Article::class.java)
    }

    suspend fun getArticle(userId: String, articleId: String): Article? {
        return firestore.collection("users").document(userId)
            .collection("articles").document(articleId)
            .get(Source.SERVER).await().toObject(Article::class.java)
    }

    suspend fun updateArticle(userId: String, article: Article) {
        firestore.collection("users").document(userId)
            .collection("articles").document(article.id)
            .set(article).await()
    }

    suspend fun deleteArticle(userId: String, articleId: String) {
        firestore.collection("users").document(userId)
            .collection("articles").document(articleId).delete().await()
    }

    suspend fun getAllArticles(): List<Article> {
        val articleRefs = firestore.collection("users").get().await().documents
            .map { it.reference.collection("articles") }

        val allArticles = mutableListOf<Article>()
        for (ref in articleRefs) {
            val articles = ref.orderBy("createdAt", Query.Direction.DESCENDING).get(Source.SERVER).await().toObjects(Article::class.java)
            allArticles.addAll(articles)
        }
        return allArticles
    }

    suspend fun searchUsers(query: String): List<UserProfile> {
        return if (query.isNotBlank()) {
            firestore.collection("users")
                .whereGreaterThanOrEqualTo("name", query)
                .whereLessThanOrEqualTo("name", query + '\uf8ff')
                .get().await().toObjects(UserProfile::class.java)
        } else {
            emptyList()
        }
    }
}