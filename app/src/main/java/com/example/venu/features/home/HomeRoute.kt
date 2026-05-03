package com.example.venu.features.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.venu.features.home.viewmodel.HomeViewModel

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToExploreDirections: (eventId: String) -> Unit
) {
    HomeScreen(
        state = viewModel.uiState,
        onAction = viewModel::onAction,
        onNavigateToExploreDirections = onNavigateToExploreDirections
    )
}