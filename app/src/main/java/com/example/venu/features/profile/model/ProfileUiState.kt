package com.example.venu.features.profile.model

data class ProfileUiState(
    val displayName: String = "Explorer",
    val isSignedIn: Boolean = false,
    val eventsCount: Int = 8,
    val reviewsCount: Int = 12,
    val streakCount: Int = 5
)

