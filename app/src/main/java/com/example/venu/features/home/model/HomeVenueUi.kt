package com.example.venu.features.home.model

import com.example.venu.core.core_domain.model.Genre

data class HomeVenueUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val latitude: Double,
    val longitude: Double,
    val ratingLabel: String? = null,
    val distanceLabel: String? = null,
    val isSaved: Boolean = false,
    val genre: Genre,
    val locationName: String = title,
    val imageUrl: String? = null
)