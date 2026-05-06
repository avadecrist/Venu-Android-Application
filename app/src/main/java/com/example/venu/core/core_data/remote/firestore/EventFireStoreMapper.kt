package com.example.venu.core.core_data.remote.firestore

import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier

fun Event.toFirestoreDto(
    createdAt: Long,
    updatedAt: Long
): EventFirestoreDto {
    return EventFirestoreDto(
        id = id,
        eventName = eventName,
        description = description,
        genre = genre.name,
        locationName = locationName,
        googlePlaceId = googlePlaceId,
        googlePlaceAddress = googlePlaceAddress,
        googleRating = googleRating,
        latitude = latitude,
        longitude = longitude,
        priceTier = priceTier.name,
        hours = hours,
        imageUrl = imageUrl,
        isVerifiedVenue = isVerifiedVenue,
        venuRating = venuRating,
        reviewCount = reviewCount,
        interestLevel = interestLevel,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun EventFirestoreDto.toDomain(): Event {
    return Event(
        id = id,
        eventName = eventName,
        description = description,
        genre = genre.toGenre(),
        locationName = locationName,
        googlePlaceId = googlePlaceId,
        googlePlaceAddress = googlePlaceAddress,
        googleRating = googleRating,
        latitude = latitude,
        longitude = longitude,
        distanceKm = null,
        priceTier = priceTier.toPriceTier(),
        hours = hours,
        imageUrl = imageUrl,
        venuRating = venuRating,
        reviewCount = reviewCount,
        isVerifiedVenue = isVerifiedVenue,
        interestLevel = interestLevel
    )
}

private fun String.toGenre(): Genre {
    return runCatching {
        Genre.valueOf(this)
    }.getOrDefault(Genre.MUSIC)
}

private fun String.toPriceTier(): PriceTier {
    return when (this) {
        "FREE" -> PriceTier.FREE
        "ONE" -> PriceTier.ONE
        "TWO" -> PriceTier.TWO
        "THREE" -> PriceTier.THREE
        "FOUR" -> PriceTier.FOUR
        "UNKNOWN" -> PriceTier.UNKNOWN

        // Legacy compatibility
        "UNDER_10" -> PriceTier.ONE
        "UNDER_20" -> PriceTier.TWO

        else -> PriceTier.UNKNOWN
    }
}