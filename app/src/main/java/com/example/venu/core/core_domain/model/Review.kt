package com.example.venu.core.core_domain.model

data class Review(
    val id: String,
    val eventId: String,
    val authorName: String, // displayName of User
    val userId: String,
    val rating: Int,          // 1-5
    val comment: String,
    val createdAtLabel: String // simple for now ("2d ago")
)