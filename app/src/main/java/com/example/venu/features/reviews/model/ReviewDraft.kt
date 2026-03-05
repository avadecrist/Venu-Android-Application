package com.example.venu.features.reviews.model

data class ReviewDraft(
    val eventId: String,
    val rating: Int = 0,
    val comment: String = ""
)