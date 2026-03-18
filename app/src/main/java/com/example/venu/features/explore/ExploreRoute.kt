package com.example.venu.features.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.venu.core.core_common.AppGraph
import com.example.venu.core.core_data.repository.FakeEventRepository
import com.example.venu.core.core_data.repository.InMemoryListsRepository
import com.example.venu.features.explore.viewmodel.ExploreViewModel

@Composable
fun ExploreRoute(
    hasLocationPermission: Boolean
) {
    // use keyword 'remember' to persist repos!
    val viewModel = remember { ExploreViewModel() }

    ExploreScreen(
        state = viewModel.uiState,
        onAction = viewModel::onAction,
        onDismissSaveSheet = viewModel::dismissSaveSheet,
        hasLocationPermission = hasLocationPermission
    )
}
