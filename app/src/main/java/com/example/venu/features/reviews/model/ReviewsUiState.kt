package com.example.venu.features.reviews.model

import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_domain.model.RatingSummary

data class ReviewsUiState(
    val eventId: String = "",
    val reviews: List<Review> = emptyList(), // list to render
    val summary: RatingSummary = RatingSummary(average = 0.0, count = 0),
    val myReview: Review? = null, // if the user already reviewed, show it (and allow edits)
    val draft: ReviewDraft? = null, // if user is writing/editing?
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)