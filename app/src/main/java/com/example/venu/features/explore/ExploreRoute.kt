package com.example.venu.features.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.venu.features.explore.viewmodel.ExploreViewModel

@Composable
fun ExploreRoute(
    hasLocationPermission: Boolean
) {
    val viewModel = remember { ExploreViewModel() }

    ExploreScreen(
        state = viewModel.uiState,
        onAction = viewModel::onAction,
        onDismissSaveSheet = viewModel::dismissSaveSheet,
        hasLocationPermission = hasLocationPermission,
    )
}
