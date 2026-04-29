package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.Genre

//move this to core common eventually

data class PlaceUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distanceKm: Double?,
    val rating: Double,
    val genre: Genre,
    val isVerified: Boolean,
    val isSaved: Boolean,
    val savedLabel: String? = null
)