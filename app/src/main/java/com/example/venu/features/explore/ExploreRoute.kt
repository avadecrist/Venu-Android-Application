package com.example.venu.features.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.venu.core.core_data.repository.FakeEventRepository
import com.example.venu.core.core_data.repository.InMemoryListsRepository
import com.example.venu.features.explore.viewmodel.ExploreViewModel

@Composable
fun ExploreRoute(
) {
    // use keyword 'remember' to persist repos!
    val eventRepo = remember { FakeEventRepository() }
    val listsRepo = remember { InMemoryListsRepository(eventRepo) }

    val viewModel = remember {
        ExploreViewModel(
            eventRepository = eventRepo,
            listsRepository = listsRepo
        )
    }
    ExploreScreen(
        state = viewModel.uiState,
        onAction = viewModel::onAction
    )
}
