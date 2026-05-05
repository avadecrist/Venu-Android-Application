package com.example.venu.features.profile.model

data class ProfileUiState(
    val displayName: String = "Explorer",
    val isSignedIn: Boolean = false,
    val photoUrl: String? = null,
    val eventsVisitedCount: Int = 0,
    val reviewsCount: Int = 0
)

