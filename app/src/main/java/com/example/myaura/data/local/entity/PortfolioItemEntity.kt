package com.example.myaura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolios")
data class PortfolioItemEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val dateRange: String,
    val skill: String,
    val projectUrl: String,
    val createdAt: Long
)