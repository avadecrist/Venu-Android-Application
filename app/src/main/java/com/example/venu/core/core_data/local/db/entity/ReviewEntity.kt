package com.example.venu.core.core_data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    indices = [
        Index(value = ["eventId"]),
        Index(value = ["userId"]),
        Index(value = ["googlePlaceId"])
    ]
)
data class ReviewEntity(
    @PrimaryKey val reviewId: String,

    val eventId: String,
    val googlePlaceId: String?,

    val uid: String,
    val displayName: String,
    val photoUrl: String?,

    val rating: Double,
    val comment: String,

    val createdAt: Long
)