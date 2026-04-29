package com.example.venu.features.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.venu.features.lists.model.ListsUiEvent
import com.example.venu.features.lists.viewmodel.ListsViewModel

@Composable
fun ListsRoute(
    viewModel: ListsViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ListsUiEvent.Refresh)
    }

    ListsScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}