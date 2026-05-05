package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier

data class GooglePlaceEventDraft(
    val name: String,              // event name shown on card
    val description: String,
    val location: String,          // Place/Venue name
    val address: String,
    val category: Genre,
    val hours: String?,
    val googlePlaceId: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?,
    val googleRating: Double?,     // Google rating, optional external signal
    val priceTier: PriceTier,
    val interestLevel: Int = 0     // later calculated from Want to Go count
)