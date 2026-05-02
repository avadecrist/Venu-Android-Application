package com.example.venu.features.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.venu.features.profile.model.ProfileUiState

@Composable
fun ProfileRoute(
    isSignedIn: Boolean,
    onSignInClick: () -> Unit,
    onMyReviewsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var displayName by rememberSaveable {
        mutableStateOf("Explorer")
    }

    ProfileScreen(
        state = ProfileUiState(
            isSignedIn = isSignedIn,
            displayName = displayName
        ),
        onSignInClick = onSignInClick,
        onEditProfileSave = { newDisplayName ->
            displayName = newDisplayName
        },
        onMyReviewsClick = onMyReviewsClick,
        onSettingsClick = onSettingsClick
    )
}