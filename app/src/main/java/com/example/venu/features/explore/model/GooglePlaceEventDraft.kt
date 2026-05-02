package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier

/**
 * Temporary bridge model between Google Places venue data and Venu user-created events.
 *
 * Later, this should be populated from the Google Places API response.
 * For now, it gives the app a clean, typed object instead of creating
 * hardcoded/debug events inside the ViewModel.
 */
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

    val imageUrl: String? = null,
    val priceTier: PriceTier = PriceTier.FREE
)
