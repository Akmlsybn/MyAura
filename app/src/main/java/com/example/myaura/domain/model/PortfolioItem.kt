package com.example.myaura.domain.model

import com.google.firebase.Timestamp

data class PortfolioItem (
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val dateRange: String = "",
    val skill: String = "",
    val projectUrl: String = "",
    val createdAt: Timestamp = Timestamp.now()
)