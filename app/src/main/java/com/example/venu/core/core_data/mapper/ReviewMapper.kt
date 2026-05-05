package com.example.venu.core.core_data.mapper

import com.example.venu.core.core_data.remote.firestore.ReviewFireStoreDto
import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_common.util.formatTimeAgo


fun ReviewFireStoreDto.toDomain(): Review {
    return Review(
        id = reviewId,
        eventId = eventId,
        googlePlaceId = googlePlaceId,
        userId = uid,
        displayName = displayName,
        photoUrl = photoUrl,
        rating = rating,
        comment = comment,
        createdAt = createdAt,
        createdAtLabel = formatTimeAgo(createdAt)
    )
}

fun Review.toDto(): ReviewFireStoreDto {
    return ReviewFireStoreDto(
        reviewId = id,
        eventId = eventId,
        googlePlaceId = googlePlaceId,
        uid = userId,
        displayName = displayName,
        photoUrl = photoUrl,
        rating = rating,
        comment = comment,
        createdAt = createdAt
    )
}