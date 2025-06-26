package com.example.myaura.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val job: String = "",
    val tagline: String = "",
    val bio: String = "",
    val profilePictureUrl: String = "",
    val socialMediaLinks: Map<String, String> = emptyMap()
)