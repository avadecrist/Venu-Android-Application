package com.example.venu.features.explore

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExploreRoute(
    viewModel: ExploreViewModel = viewModel()
) {
    ExploreScreen(
        state = viewModel.uiState,
        onAction = viewModel::onAction
    )
}
