package com.example.venu.features.reviews.model

sealed interface ReviewsAction {
    data class LoadReviews(val eventId: String) : ReviewsAction
    data class UpdateRating(val rating: Int) : ReviewsAction
    data class UpdateComment(val comment: String) : ReviewsAction
    object Submit : ReviewsAction
    object Cancel : ReviewsAction
}