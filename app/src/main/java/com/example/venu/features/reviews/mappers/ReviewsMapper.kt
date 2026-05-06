package com.example.venu.features.reviews.mappers

import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_presentation.ReviewUi

fun Review.toUi(): ReviewUi {
    return ReviewUi(
        id = id,
        displayName = displayName,
        photoUrl = photoUrl,
        rating = rating,
        comment = comment,
        timeAgo = createdAtLabel
    )
}