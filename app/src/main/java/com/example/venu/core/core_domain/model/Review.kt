package com.example.venu.core.core_domain.model

data class Review(
    val id: String,
    val eventId: String,
    val googlePlaceId: String?,
    val userId: String,
    val displayName: String,
    val photoUrl: String?,
    val rating: Int, // from 1-5
    val comment: String,
    val createdAt: Long,
    val createdAtLabel: String
)