package com.example.venu.features.reviews.model

sealed interface ReviewsUiEvent {
    data class StartReview(val eventId: String) : ReviewsUiEvent
    data class UpdateRating(val rating: Int) : ReviewsUiEvent
    data class UpdateComment(val comment: String) : ReviewsUiEvent
    object Submit : ReviewsUiEvent
    object Cancel : ReviewsUiEvent
}