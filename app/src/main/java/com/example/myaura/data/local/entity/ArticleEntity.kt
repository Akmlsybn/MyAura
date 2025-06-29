package com.example.myaura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val subTitle: String,
    val content: String,
    val imageUrl: String,
    val createdAt: Long
)