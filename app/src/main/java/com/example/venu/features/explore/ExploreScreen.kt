package com.example.venu.features.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.venu.features.explore.model.ExploreAction
import com.example.venu.features.explore.model.ExploreGenre
import com.example.venu.features.explore.model.ExploreUiState
import com.example.venu.features.explore.model.PlaceUi
import com.example.venu.features.lists.tabLabel

private val ExploreSheetPeekHeight = 120.dp
private const val ExploreSheetExpandedFraction = 0.86f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    state: ExploreUiState,
    onAction: (ExploreAction) -> Unit,
    onDismissSaveSheet: () -> Unit,
    hasLocationPermission: Boolean
) {
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        sheetPeekHeight = ExploreSheetPeekHeight,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetDragHandle = { BottomSheetDefaults.DragHandle() },
        sheetContent = {
            ExploreResultsSheet(
                places = state.places,
                selectedPlaceId = state.selectedPlaceId,
                onPlaceClicked = { id ->
                    onAction(ExploreAction.PlaceClicked(id))
                },
                onSaveClick = { id ->
                    onAction(ExploreAction.SaveClicked(id))
                },
                modifier = Modifier.fillMaxHeight(ExploreSheetExpandedFraction)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ExploreMap(
                modifier = Modifier.fillMaxSize(),
                places = state.places,
                selectedPlaceId = state.selectedPlaceId,
                onMarkerSelected = { id ->
                    onAction(ExploreAction.PlaceClicked(id))
                }
            )

            ExploreTopControls(
                query = state.query,
                selectedGenre = state.selectedGenre,
                onQueryChange = { onAction(ExploreAction.QueryChanged(it)) },
                onGenreSelected = { onAction(ExploreAction.GenreSelected(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
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
                            Icon(Icons.Default.Bookmark, contentDescription = null)
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
private fun ExploreTopControls(
    query: String,
    selectedGenre: ExploreGenre?,
    onQueryChange: (String) -> Unit,
    onGenreSelected: (ExploreGenre?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Explore",
            style = MaterialTheme.typography.headlineLarge
        )

        Card(shape = RoundedCornerShape(24.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                label = { Text("Search venues") },
                singleLine = true
            )
        }

        Card(shape = RoundedCornerShape(24.dp)) {
            GenreChipsRow(
                selected = selectedGenre,
                onSelected = onGenreSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 14.dp)
            )
        }
    }
}

@Composable
private fun ExploreResultsSheet(
    places: List<PlaceUi>,
    selectedPlaceId: String?,
    onPlaceClicked: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (places.isEmpty()) "No events found" else "Events nearby",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = if (places.isEmpty()) {
                    "Try changing your search or genre filters."
                } else {
                    "${places.size} places on the map"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(8.dp))

        if (places.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Swipe the sheet down or change filters to explore a different area.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(places, key = { it.id }) { place ->
                    PlaceCard(
                        place = place,
                        selected = place.id == selectedPlaceId,
                        onClick = { onPlaceClicked(place.id) },
                        onSaveClick = { onSaveClick(place.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GenreChipsRow(
    selected: ExploreGenre?,
    onSelected: (ExploreGenre?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelected(null) },
                label = { Text("All") }
            )
        }

        items(ExploreGenre.entries) { genre ->
            FilterChip(
                selected = selected == genre,
                onClick = { onSelected(genre) },
                label = { Text(genre.label) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreScreenPreview() {
    ExploreScreen(
        state = ExploreUiState(),
        onAction = {},
        onDismissSaveSheet = {},
        hasLocationPermission = false
    )
}