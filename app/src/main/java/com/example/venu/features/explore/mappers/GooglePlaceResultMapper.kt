package com.example.venu.features.explore.mappers

import com.example.venu.core.core_data.places.GooglePlaceResult
import com.example.venu.core.core_data.places.GooglePlaceSuggestion
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier
import com.example.venu.features.explore.model.GooglePlaceEventDraft
import com.example.venu.features.explore.model.GooglePlaceSuggestionUi

fun GooglePlaceSuggestion.toUi(): GooglePlaceSuggestionUi {
    return GooglePlaceSuggestionUi(
        placeId = placeId,
        primaryText = primaryText,
        secondaryText = secondaryText
    )
}

fun GooglePlaceResult.toEventDraft(): GooglePlaceEventDraft {
    return GooglePlaceEventDraft(
        name = "",
        description = "",
        location = name,
        address = address.orEmpty(),
        category = Genre.MUSIC,
        hours = hours,
        googlePlaceId = placeId,
        latitude = latitude,
        longitude = longitude,
        imageUrl = photoUrl,
        googleRating = rating,
        priceTier = PriceTier.FREE,
        interestLevel = 0
    )
}