package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.core.core_domain.model.Genre

data class PlaceUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val locationName: String = name,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double?,
    val rating: Double,
    val googleRating: Double?,
    val genre: Genre,
    val isVerified: Boolean,
    val isSaved: Boolean,
    val savedLabel: String?,
    val imageUrl: String? = null,
    val priceText: String,
    val hours: String,
    val reviewCount: Int = 0,
    val attendeeCount: Int = 0, //refactor to interestLevel
    val crowdLevel: CrowdLevel = CrowdLevel.UNKNOWN
)