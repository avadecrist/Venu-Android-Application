package com.example.venu.core.core_data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val subtitle: String,
    val genre: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double?,
    val priceTier: String,
    val startTimeLabel: String,
    val imageUrl: String?,
    val credibilityScore: Int,
    val reviewCount: Int,
    val isVerifiedVenue: Boolean,
    val averageRating: Double,
//    val attendeeCount: Int,
//    val crowdLevel: String,
)