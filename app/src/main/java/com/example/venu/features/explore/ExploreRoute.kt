package com.example.venu.features.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.venu.features.explore.viewmodel.ExploreViewModel

@Composable
fun ExploreRoute(
    hasLocationPermission: Boolean,
    eventId: String?,
    startDirections: Boolean
) {
    val viewModel = remember { ExploreViewModel() }

    LaunchedEffect(eventId, startDirections) {
        if (eventId != null && startDirections) {
            viewModel.openFromHomeForDirections(eventId)
        }
    }

    ExploreScreen(
        state = viewModel.uiState,
        onAction = viewModel::onAction,
        onDismissSaveSheet = viewModel::dismissSaveSheet,
        hasLocationPermission = hasLocationPermission,
    )
}
