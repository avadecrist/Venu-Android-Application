package com.example.venu.features.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.venu.features.explore.model.ExploreAction
import com.example.venu.features.explore.model.ExploreGenre
import com.example.venu.features.explore.model.ExploreUiState
import com.example.venu.features.lists.tabLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    state: ExploreUiState,
    onAction: (ExploreAction) -> Unit,
    onDismissSaveSheet: () -> Unit,
    hasLocationPermission: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Explore", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.query,
            onValueChange = { onAction(ExploreAction.QueryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search venues") },
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        GenreChipsRow(
            selected = state.selectedGenre,
            onSelected = { onAction(ExploreAction.GenreSelected(it)) }
        )

        Spacer(Modifier.height(12.dp))

        // Map placeholder (Sprint 1)
        Card(modifier = Modifier.fillMaxWidth()) {
            ExploreMap(
                places = state.places,
                selectedPlaceId = state.selectedPlaceId,
                onMarkerSelected = { id -> onAction(ExploreAction.PlaceClicked(id)) }
            )
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.places, key = { it.id }) { place ->
                PlaceCard(
                    place = place,
                    selected = place.id == state.selectedPlaceId,
                    onClick = { onAction(ExploreAction.PlaceClicked(place.id)) },
                    onSaveClick = { onAction(ExploreAction.SaveClicked(place.id)) }
                )
            }
        }
    }
    if (state.showSaveSheet && state.pendingSaveEventId != null) {

        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = onDismissSaveSheet,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Save to list",
                    style = MaterialTheme.typography.titleLarge
                )

                state.availableLists.forEach { listType ->
                    TextButton(
                        onClick = {
                            onAction(
                                ExploreAction.SaveToList(
                                    eventId = state.pendingSaveEventId,
                                    listType = listType
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(Icons.Default.Bookmark, contentDescription = null) // or Icons.Default.PlaylistAdd / Icons.Default.List
                            Spacer(Modifier.width(12.dp))
                            Text(tabLabel(listType))
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}


@Composable
private fun GenreChipsRow(
    selected: ExploreGenre?,
    onSelected: (ExploreGenre?) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelected(null) },
                label = { Text("All") }
            )
        }

        // ✅ Key fix: use the iterable overload, not the Int-count overload
        items(ExploreGenre.entries) { genre ->
            FilterChip(
                selected = selected == genre,
                onClick = { onSelected(genre) },
                label = { Text(genre.label) } // requires ExploreGenre(val label: String)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreScreenPreview() {
    ExploreScreen(
        state = ExploreUiState(), // hard code in a List of PlaceUi events to see in preview
        onAction = {},
        onDismissSaveSheet = {},
        hasLocationPermission = false
    )
}