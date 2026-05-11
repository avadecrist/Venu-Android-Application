package com.example.venu.features.home.model

import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.core.core_domain.model.Genre

data class HomeVenueUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String = title,
    val ratingLabel: String? = null,
    val googleRating: Double?,
    val distanceLabel: String? = null,
    val isSaved: Boolean = false,
    val genre: Genre,
    val imageUrl: String? = null,
    val priceText: String,
    val startTimeLabel: String,
    val reviewCount: Int = 0,
    val attendeeCount: Int = 0,
    val crowdLevel: CrowdLevel = CrowdLevel.UNKNOWN
)