package com.example.venu.features.profile

import androidx.compose.runtime.Composable
import com.example.venu.features.profile.model.ProfileUiState

@Composable
fun ProfileRoute(
    isSignedIn: Boolean,
    onSignInClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    ProfileScreen(
        state = ProfileUiState(
            isSignedIn = isSignedIn
        ),
        onSignInClick = onSignInClick,
        onSettingsClick = onSettingsClick
    )
}