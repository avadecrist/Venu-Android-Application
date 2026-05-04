package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier

data class GooglePlaceEventDraft(
    val eventName: String,
    val eventSubtitle: String,
    val genre: Genre,
    val startTimeLabel: String,
    val googlePlaceId: String,
    val venueName: String,
    val googlePlaceAddress: String?,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?,
    val rating: Double?,
    val priceTier: PriceTier
)