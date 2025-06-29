package com.example.myaura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myaura.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles WHERE userId = :userId ORDER BY createdAt DESC")
    fun getArticlesByUserId(userId: String): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :articleId")
    suspend fun getArticleById(articleId: String): ArticleEntity?

    @Query("DELETE FROM articles WHERE userId = :userId")
    suspend fun deleteAllArticlesOfUser(userId: String)

    @Query("DELETE FROM articles WHERE id = :articleId")
    suspend fun deleteArticleById(articleId: String) //
}