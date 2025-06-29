package com.example.myaura.data.local.mapper

import com.example.myaura.data.local.entity.ArticleEntity
import com.example.myaura.data.local.entity.PortfolioItemEntity
import com.example.myaura.data.local.entity.UserProfileEntity
import com.example.myaura.data.local.entity.UserSessionEntity
import com.example.myaura.domain.model.Article
import com.example.myaura.domain.model.PortfolioItem
import com.example.myaura.domain.model.UserProfile
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth

fun ArticleEntity.toDomain(): Article {
    return Article(
        id = this.id,
        userId = this.userId,
        title = this.title,
        subTitle = this.subTitle,
        content = this.content,
        imageUrl = this.imageUrl,
        createdAt = Timestamp(this.createdAt, 0)
    )
}

fun Article.toEntity(): ArticleEntity {
    return ArticleEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        subTitle = this.subTitle,
        content = this.content,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.seconds
    )
}

fun PortfolioItemEntity.toDomain(): PortfolioItem {
    return PortfolioItem(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        dateRange = this.dateRange,
        skill = this.skill,
        projectUrl = this.projectUrl,
        createdAt = Timestamp(this.createdAt, 0)
    )
}

fun PortfolioItem.toEntity(): PortfolioItemEntity {
    return PortfolioItemEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        dateRange = this.dateRange,
        skill = this.skill,
        projectUrl = this.projectUrl,
        createdAt = this.createdAt.seconds
    )
}

fun UserProfileEntity.toDomain(): UserProfile {
    val socialMediaLinksType = object : TypeToken<Map<String, String>>() {}.type
    val socialMediaLinks: Map<String, String> = Gson().fromJson(this.socialMediaLinksJson, socialMediaLinksType)

    return UserProfile(
        uid = this.uid,
        name = this.name,
        job = this.job,
        tagline = this.tagline,
        bio = this.bio,
        profilePictureUrl = this.profilePictureUrl,
        socialMediaLinks = socialMediaLinks
    )
}

fun UserProfile.toEntity(): UserProfileEntity {
    val socialMediaLinksJson = Gson().toJson(this.socialMediaLinks)
    return UserProfileEntity(
        uid = this.uid,
        name = this.name,
        job = this.job,
        tagline = this.tagline,
        bio = this.bio,
        profilePictureUrl = this.profilePictureUrl,
        socialMediaLinksJson = socialMediaLinksJson
    )
}

fun UserSessionEntity.toDomain(auth: FirebaseAuth): Boolean {
    return this.isLoggedIn && auth.currentUser != null
}

fun Boolean.toEntity(): UserSessionEntity {
    return UserSessionEntity(
        id = 1,
        isLoggedIn = this,
        isDarkMode = false
    )
}