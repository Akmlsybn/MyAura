package com.example.myaura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val job: String,
    val tagline: String,
    val bio: String,
    val profilePictureUrl: String,
    val socialMediaLinksJson: String
)