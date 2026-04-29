package com.example.venu.core.core_presentation

import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.core.core_domain.model.Genre

data class EventDetailsUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val genre: Genre,
    val locationName: String,
    val distanceKm: Double?,
    val priceText: String,
    val startTimeLabel: String,
    val imageUrl: String? = null,
    val credibilityScore: Int,
    val reviewCount: Int,
    val isVerifiedVenue: Boolean,
    val averageRating: Double,
    val googleRating: Double,
    val userRating: Double,
    val attendeeCount: Int,
    val crowdLevel: CrowdLevel,
    val reviews: List<ReviewUi>,
    val isSaved: Boolean = false
)