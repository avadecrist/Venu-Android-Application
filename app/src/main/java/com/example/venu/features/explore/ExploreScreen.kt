package com.example.venu.features.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.venu.features.explore.model.ExploreAction
import com.example.venu.features.explore.model.ExploreGenre
import com.example.venu.features.explore.model.ExploreUiState

@Composable
fun ExploreScreen(
    state: ExploreUiState,
    onAction: (ExploreAction) -> Unit
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .padding(14.dp)
            ) {
                Text(
                    text = "Map coming soon (Sprint 2+)",
                    style = MaterialTheme.typography.titleMedium
                )
            }
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
                    onToggleSaved = { onAction(ExploreAction.ToggleSaved(place.id)) }
                )
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
        onAction = {}
    )
}