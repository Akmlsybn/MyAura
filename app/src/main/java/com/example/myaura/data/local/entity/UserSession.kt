package com.example.myaura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_session")
data class UserSessionEntity(
    @PrimaryKey val id: Int = 1,
    val isLoggedIn: Boolean,
    val isDarkMode: Boolean
)