package com.example.venu.features.profile

import androidx.compose.runtime.Composable
import com.example.venu.features.profile.model.ProfileUiState

@Composable
fun ProfileRoute() {
    ProfileScreen(
        state = ProfileUiState()
    )
}