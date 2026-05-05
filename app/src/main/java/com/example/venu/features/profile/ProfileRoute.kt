package com.example.venu.features.profile

import androidx.compose.runtime.Composable
import com.example.venu.features.profile.model.ProfileUiState

@Composable
fun ProfileRoute(
    isSignedIn: Boolean,
    displayName: String?,
    email: String?,
    reviewsCount: Int,
    eventsVisitedCount: Int,
    onSignInClick: () -> Unit,
    onEditProfileSave: (String) -> Unit,
    onMyReviewsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    ProfileScreen(
        state = ProfileUiState(
            isSignedIn = isSignedIn,
            displayName = displayName ?: email ?: "Explorer",
            reviewsCount = reviewsCount,
            eventsVisitedCount = eventsVisitedCount
        ),
        onSignInClick = onSignInClick,
        onEditProfileSave = onEditProfileSave,
        onMyReviewsClick = onMyReviewsClick,
        onSettingsClick = onSettingsClick
    )
}