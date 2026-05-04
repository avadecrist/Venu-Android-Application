package com.example.venu.features.profile

import androidx.compose.runtime.Composable
import com.example.venu.features.profile.model.ProfileUiState

@Composable
fun ProfileRoute(
    isSignedIn: Boolean,
    displayName: String?,
    email: String?,
    onSignInClick: () -> Unit,
    onEditProfileSave: (String) -> Unit,
    onMyReviewsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    ProfileScreen(
        state = ProfileUiState(
            isSignedIn = isSignedIn,
            displayName = displayName ?: email ?: "Explorer"
        ),
        onSignInClick = onSignInClick,
        onEditProfileSave = onEditProfileSave,
        onMyReviewsClick = onMyReviewsClick,
        onSettingsClick = onSettingsClick
    )
}