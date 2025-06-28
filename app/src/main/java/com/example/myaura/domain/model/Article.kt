package com.example.myaura.domain.model

import com.google.firebase.Timestamp

data class Article (
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val subTitle: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val createdAt: Timestamp = Timestamp.now()
)