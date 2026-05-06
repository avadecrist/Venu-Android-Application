package com.example.venu.features.reviews.mappers

import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_presentation.ReviewUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Review.toReviewUi(): ReviewUi {
    return ReviewUi(
        displayName = displayName,
        photoUrl = photoUrl.orEmpty(),
        rating = rating,
        comment = comment,
        timeAgo = createdAt.toDateLabel(),
        id = id
    )
}

private fun Long.toDateLabel(): String {
    return SimpleDateFormat(
        "MMM d, yyyy",
        Locale.getDefault()
    ).format(Date(this))
}