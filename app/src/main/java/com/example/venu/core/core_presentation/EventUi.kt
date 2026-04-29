package com.example.venu.core.core_presentation

import com.example.venu.core.core_domain.model.Genre

data class EventUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distanceKm: Double? = null,
    val rating: Double? = null,
    val genre: Genre? = null,
    val isVerified: Boolean = false,
    val isSaved: Boolean = false,
    val savedLabel: String? = null
)