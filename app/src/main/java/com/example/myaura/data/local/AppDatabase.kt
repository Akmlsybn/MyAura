package com.example.myaura.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myaura.data.local.converter.Converters
import com.example.myaura.data.local.dao.ArticleDao
import com.example.myaura.data.local.dao.PortfolioDao
import com.example.myaura.data.local.dao.UserProfileDao
import com.example.myaura.data.local.dao.UserSessionDao
import com.example.myaura.data.local.entity.ArticleEntity
import com.example.myaura.data.local.entity.PortfolioItemEntity
import com.example.myaura.data.local.entity.UserProfileEntity
import com.example.myaura.data.local.entity.UserSessionEntity

@Database(
    entities = [ArticleEntity::class, PortfolioItemEntity::class, UserProfileEntity::class, UserSessionEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun userSessionDao(): UserSessionDao
}