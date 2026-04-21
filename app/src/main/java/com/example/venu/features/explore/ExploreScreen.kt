package com.example.venu.features.explore

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private enum class ExploreSortOption(val label: String) {
    FEATURED("Featured"),
    DISTANCE("Distance"),
    RATING("Rating"),
    NAME("Name")
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNUSED_PARAMETER")
@Composable
fun ExploreScreen(
    state: ExploreUiState,
    onAction: (ExploreAction) -> Unit,
    onDismissSaveSheet: () -> Unit,
    hasLocationPermission: Boolean
) {
    var showFilterSortDialog by remember { mutableStateOf(false) }
    var selectedGenres by remember { mutableStateOf(setOf<ExploreGenre>()) }
    var verifiedOnly by remember { mutableStateOf(false) }
    var savedOnly by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf(ExploreSortOption.FEATURED) }

    val displayedPlaces = remember(
        state.places,
        selectedGenres,
        verifiedOnly,
        savedOnly,
        sortOption
    ) {
        val filtered = state.places.filter { place ->
            val genreMatches = selectedGenres.isEmpty() || place.genre in selectedGenres
            val verifiedMatches = !verifiedOnly || place.isVerified
            val savedMatches = !savedOnly || place.isSaved || place.savedLabel != null

            genreMatches && verifiedMatches && savedMatches
        }

        when (sortOption) {
            ExploreSortOption.FEATURED -> filtered
            ExploreSortOption.DISTANCE -> filtered.sortedBy { it.distanceKm ?: Double.MAX_VALUE }
            ExploreSortOption.RATING -> filtered.sortedByDescending { it.rating }
            ExploreSortOption.NAME -> filtered.sortedBy { it.name.lowercase() }
        }
    }

    val activeFilterCount = selectedGenres.size +
            if (verifiedOnly) 1 else 0 +
                    if (savedOnly) 1 else 0

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
        sheetDragHandle = { BottomSheetDefaults.DragHandle() },
        sheetContent = {
            ExploreResultsSheet(
                places = displayedPlaces,
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
                places = displayedPlaces,
                selectedPlaceId = state.selectedPlaceId,
                onMarkerSelected = { id ->
                    onAction(ExploreAction.PlaceClicked(id))
                }
            )

            ExploreTopControls(
                query = state.query,
                onQueryChange = { onAction(ExploreAction.QueryChanged(it)) },
                onOpenFilterSort = { showFilterSortDialog = true },
                activeFilterCount = activeFilterCount,
                sortOption = sortOption,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }

    if (showFilterSortDialog) {
        ExploreFilterSortDialog(
            currentGenres = selectedGenres,
            currentVerifiedOnly = verifiedOnly,
            currentSavedOnly = savedOnly,
            currentSortOption = sortOption,
            onDismiss = { showFilterSortDialog = false },
            onApply = { genres, verified, saved, sort ->
                selectedGenres = genres
                verifiedOnly = verified
                savedOnly = saved
                sortOption = sort
                showFilterSortDialog = false
            }
        )
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
    onQueryChange: (String) -> Unit,
    onOpenFilterSort: () -> Unit,
    activeFilterCount: Int,
    sortOption: ExploreSortOption,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Explore",
                style = MaterialTheme.typography.headlineLarge
            )

            FilledTonalIconButton(onClick = onOpenFilterSort) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Open filter and sort"
                )
            }
        }

        Card {
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

        if (activeFilterCount > 0 || sortOption != ExploreSortOption.FEATURED) {
            Card {
                Text(
                    text = buildString {
                        append("$activeFilterCount active filter")
                        if (activeFilterCount != 1) append("s")
                        append(" • Sort: ${sortOption.label}")
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
                    "Try changing your search, filters, or sort."
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
                    text = "Open the filter/sort button in the top-right to adjust what you see.",
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
private fun ExploreFilterSortDialog(
    currentGenres: Set<ExploreGenre>,
    currentVerifiedOnly: Boolean,
    currentSavedOnly: Boolean,
    currentSortOption: ExploreSortOption,
    onDismiss: () -> Unit,
    onApply: (
        genres: Set<ExploreGenre>,
        verifiedOnly: Boolean,
        savedOnly: Boolean,
        sortOption: ExploreSortOption
    ) -> Unit
) {
    var tempGenres by remember(currentGenres) { mutableStateOf(currentGenres) }
    var tempVerifiedOnly by remember(currentVerifiedOnly) { mutableStateOf(currentVerifiedOnly) }
    var tempSavedOnly by remember(currentSavedOnly) { mutableStateOf(currentSavedOnly) }
    var tempSortOption by remember(currentSortOption) { mutableStateOf(currentSortOption) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Filter & sort")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleMedium
                )

                DialogCheckboxRow(
                    label = "Verified only",
                    checked = tempVerifiedOnly,
                    onCheckedChange = { tempVerifiedOnly = it }
                )

                DialogCheckboxRow(
                    label = "Saved only",
                    checked = tempSavedOnly,
                    onCheckedChange = { tempSavedOnly = it }
                )

                Text(
                    text = "Genres",
                    style = MaterialTheme.typography.titleSmall
                )

                ExploreGenre.entries.forEach { genre ->
                    DialogCheckboxRow(
                        label = genre.label,
                        checked = genre in tempGenres,
                        onCheckedChange = { checked ->
                            tempGenres = if (checked) {
                                tempGenres + genre
                            } else {
                                tempGenres - genre
                            }
                        }
                    )
                }

                HorizontalDivider()

                Text(
                    text = "Sort by",
                    style = MaterialTheme.typography.titleMedium
                )

                ExploreSortOption.entries.forEach { option ->
                    DialogRadioRow(
                        label = option.label,
                        selected = tempSortOption == option,
                        onClick = { tempSortOption = option }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApply(
                        tempGenres,
                        tempVerifiedOnly,
                        tempSavedOnly,
                        tempSortOption
                    )
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            Row {
                TextButton(
                    onClick = {
                        tempGenres = emptySet()
                        tempVerifiedOnly = false
                        tempSavedOnly = false
                        tempSortOption = ExploreSortOption.FEATURED
                    }
                ) {
                    Text("Reset")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Composable
private fun DialogCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

@Composable
private fun DialogRadioRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(Modifier.width(8.dp))
        Text(label)
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