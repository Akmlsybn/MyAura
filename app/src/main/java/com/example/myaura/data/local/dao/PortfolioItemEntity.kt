package com.example.myaura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myaura.data.local.entity.PortfolioItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolios(portfolios: List<PortfolioItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolio(portfolio: PortfolioItemEntity)

    @Update
    suspend fun updatePortfolio(portfolio: PortfolioItemEntity)

    @Query("SELECT * FROM portfolios WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPortfoliosByUserId(userId: String): Flow<List<PortfolioItemEntity>>

    @Query("SELECT * FROM portfolios WHERE id = :portfolioId AND userId = :userId")
    suspend fun getPortfolioById(userId: String, portfolioId: String): PortfolioItemEntity?

    @Query("DELETE FROM portfolios WHERE id = :portfolioId AND userId = :userId")
    suspend fun deletePortfolioById(userId: String, portfolioId: String)

    @Query("DELETE FROM portfolios WHERE userId = :userId")
    suspend fun deleteAllPortfoliosOfUser(userId: String)
}