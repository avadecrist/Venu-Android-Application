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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.eventdetails.EventDetailsSheet
import com.example.venu.core.core_common.eventdetails.SaveToListSheet
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.label
import com.example.venu.core.core_presentation.toEventDetailsUi
import com.example.venu.features.explore.model.ExploreAction
import com.example.venu.features.explore.model.ExploreUiState
import com.example.venu.features.explore.model.GooglePlaceSuggestionUi
import com.example.venu.features.explore.model.PlaceUi
import kotlinx.coroutines.launch
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Button
import com.example.venu.features.explore.model.GooglePlaceEventDraft


private val ExploreSheetPeekHeight = 120.dp
private const val ExploreSheetExpandedFraction = 0.86f
private const val MAX_GOOGLE_PLACE_SUGGESTIONS = 5

private enum class ExploreSortOption(val label: String) {
    FEATURED("Featured"),
    DISTANCE("Distance"),
    RATING("Rating"),
    NAME("Name")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    state: ExploreUiState,
    onAction: (ExploreAction) -> Unit,
    onDismissSaveSheet: () -> Unit,
    hasLocationPermission: Boolean
) {
    var showFilterSortDialog by remember { mutableStateOf(false) }
    var selectedGenres by remember { mutableStateOf(setOf<Genre>()) }
    var verifiedOnly by remember { mutableStateOf(false) }
    var savedOnly by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf(ExploreSortOption.FEATURED) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var userLocation by remember {
        mutableStateOf<Location?>(null)
    }

    val locationManager = remember {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    DisposableEffect(hasLocationPermission) {
        if (!hasLocationPermission) {
            userLocation = null
            return@DisposableEffect onDispose {}
        }

        val hasFinePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFinePermission && !hasCoarsePermission) {
            userLocation = null
            return@DisposableEffect onDispose {}
        }

        val provider = LocationManager.GPS_PROVIDER

        val listener = android.location.LocationListener { location ->
            userLocation = location
        }

        try {
            userLocation = locationManager.getLastKnownLocation(provider)

            locationManager.requestLocationUpdates(
                provider,
                5000L,
                10f,
                listener
            )
        } catch (_: SecurityException) {
            userLocation = null
        }

        onDispose {
            locationManager.removeUpdates(listener)
        }
    }

    val displayedPlaces = remember(
        state.places,
        selectedGenres,
        verifiedOnly,
        savedOnly,
        sortOption
    ) {
        filterAndSortPlaces(
            places = state.places,
            selectedGenres = selectedGenres,
            verifiedOnly = verifiedOnly,
            savedOnly = savedOnly,
            sortOption = sortOption
        )
    }

    val googlePreviewPlace: PlaceUi? = remember(state.selectedGooglePlacePreview) {
        state.selectedGooglePlacePreview?.toPreviewPlaceUi()
    }

    val displayedMapPlaces: List<PlaceUi> = remember(displayedPlaces, googlePreviewPlace) {
        if (googlePreviewPlace == null) {
            displayedPlaces
        } else {
            displayedPlaces + googlePreviewPlace
        }
    }

    val selectedMapPlaceId: String? = googlePreviewPlace?.id ?: state.selectedPlaceId

    val selectedPlace = displayedPlaces.firstOrNull { place ->
        place.id == state.selectedPlaceId
    }

    val selectedEventDetails = selectedPlace?.toEventDetailsUi()

    LaunchedEffect(
        state.selectedPlaceId,
        state.shouldStartDirections,
        userLocation
    ) {
        val event = selectedEventDetails
        val location = userLocation

        if (
            state.shouldStartDirections &&
            event != null &&
            location != null
        ) {
            onAction(
                ExploreAction.GetDirectionsClicked(
                    event = event,
                    userLat = location.latitude,
                    userLng = location.longitude
                )
            )
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
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle()
        },
        sheetContent = {
            ExploreResultsSheet(
                places = displayedPlaces,
                selectedPlaceId = state.selectedPlaceId,
                onPlaceClicked = { id ->
                    scope.launch {
                        onAction(ExploreAction.PlaceClicked(id))
                        bottomSheetState.partialExpand()
                    }
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
            ExploreMapContent(
                state = state,
                displayedPlaces = displayedMapPlaces,
                selectedMapPlaceId = selectedMapPlaceId,
                activeFilterCount = activeFilterCount,
                sortOption = sortOption,
                hasLocationPermission = hasLocationPermission,
                onAction = onAction,
                onOpenFilterSort = { showFilterSortDialog = true },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    if (showFilterSortDialog) {
        ExploreFilterSortDialog(
            currentGenres = selectedGenres,
            currentVerifiedOnly = verifiedOnly,
            currentSavedOnly = savedOnly,
            currentSortOption = sortOption,
            onDismiss = {
                showFilterSortDialog = false
            },
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
        SaveToListSheet(
            pendingSaveEventId = state.pendingSaveEventId,
            availableLists = state.availableLists,
            onSaveToList = { eventId, listType ->
                onAction(
                    ExploreAction.SaveToList(
                        eventId = eventId,
                        listType = listType
                    )
                )
            },
            onDismiss = onDismissSaveSheet
        )
    }

    if (selectedEventDetails != null) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    onAction(ExploreAction.PlaceDetailsDismissed)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        ) {
            EventDetailsSheet(
                event = selectedEventDetails,
                onBack = {
                    scope.launch {
                        sheetState.hide()
                        onAction(ExploreAction.PlaceDetailsDismissed)
                    }
                },
                onSaveClick = {
                    onAction(ExploreAction.SaveClicked(selectedEventDetails.id))
                },
                onGetDirectionsClick = {
                    val location = userLocation ?: return@EventDetailsSheet

                    Log.d("DirectionsDebug", "userLocation = $location")
                    Log.d("DirectionsDebug", "selectedEventDetails = $selectedEventDetails")

                    if (location == null) {
                        Log.d("DirectionsDebug", "No user location yet")
                        return@EventDetailsSheet
                    }


                    scope.launch {
                        sheetState.hide()

                        onAction(
                            ExploreAction.GetDirectionsClicked(
                                event = selectedEventDetails,
                                userLat = location.latitude,
                                userLng = location.longitude
                            )
                        )
                    }
                },
                onSubmitReview = { _, _ -> }
            )
        }
    }
    state.selectedGooglePlacePreview?.let { draft ->
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    onAction(ExploreAction.GooglePlacePreviewDismissed)
                }
            }
        ) {
            GooglePlacePreviewSheet(
                draft = draft,
                isLoading = state.isCreatingGooglePlaceEvent,
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                        onAction(ExploreAction.GooglePlacePreviewDismissed)
                    }
                },
                onCreateEvent = {
                    onAction(ExploreAction.GooglePlaceCreateClicked(draft))
                }
            )
        }
    }
    state.pendingGooglePlaceDraft?.let { draft ->
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    onAction(ExploreAction.GooglePlaceDraftDismissed)
                }
            }
        ) {
            GooglePlaceEventDraftSheet(
                draft = draft,
                isCreating = state.isCreatingGooglePlaceEvent,
                onDraftChange = { updatedDraft ->
                    onAction(ExploreAction.GooglePlaceDraftChanged(updatedDraft))
                },
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                        onAction(ExploreAction.GooglePlaceDraftDismissed)
                    }
                },
                onCreateEvent = {
                    onAction(ExploreAction.GooglePlaceCreateConfirmed(draft))
                }
            )
        }
    }
}

@Composable
private fun ExploreMapContent(
    state: ExploreUiState,
    displayedPlaces: List<PlaceUi>,
    selectedMapPlaceId: String?,
    activeFilterCount: Int,
    sortOption: ExploreSortOption,
    hasLocationPermission: Boolean,
    onAction: (ExploreAction) -> Unit,
    onOpenFilterSort: () -> Unit,
    modifier: Modifier = Modifier
) {
    var zoomRequest by remember { mutableStateOf(0) }
    var zoomDelta by remember { mutableStateOf(0f) }

    Box(modifier = modifier) {
        ExploreMap(
            modifier = Modifier.fillMaxSize(),
            places = displayedPlaces,
            selectedPlaceId = selectedMapPlaceId,
            directionsRoute = state.directionsRoute,
            hasLocationPermission = hasLocationPermission,
            zoomRequest = zoomRequest,
            zoomDelta = zoomDelta,
            onMarkerSelected = { id ->
                if (!id.startsWith(GOOGLE_PREVIEW_PLACE_ID_PREFIX)) {
                    onAction(ExploreAction.PlaceClicked(id))
                }
            }
        )

        ExploreTopControls(
            query = state.query,
            googlePlaceSuggestions = state.googlePlaceSuggestions,
            isSearchingGooglePlaces = state.isSearchingGooglePlaces,
            isCreatingGooglePlaceEvent = state.isCreatingGooglePlaceEvent,
            googlePlacesError = state.googlePlacesError,
            onQueryChange = {
                onAction(ExploreAction.QueryChanged(it))
            },
            onGooglePlaceSuggestionClicked = { placeId ->
                onAction(ExploreAction.GooglePlaceSuggestionClicked(placeId))
            },
            onDismissGooglePlacesError = {
                onAction(ExploreAction.GooglePlacesErrorDismissed)
            },
            onOpenFilterSort = onOpenFilterSort,
            onZoomIn = {
                zoomDelta = 1f
                zoomRequest += 1
            },
            onZoomOut = {
                zoomDelta = -1f
                zoomRequest += 1
            },
            activeFilterCount = activeFilterCount,
            sortOption = sortOption,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}

@Composable
private fun ExploreTopControls(
    query: String,
    googlePlaceSuggestions: List<GooglePlaceSuggestionUi>,
    isSearchingGooglePlaces: Boolean,
    isCreatingGooglePlaceEvent: Boolean,
    googlePlacesError: String?,
    onQueryChange: (String) -> Unit,
    onGooglePlaceSuggestionClicked: (String) -> Unit,
    onDismissGooglePlacesError: () -> Unit,
    onOpenFilterSort: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(onClick = onZoomOut) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Zoom out"
                    )
                }

                FilledTonalIconButton(onClick = onZoomIn) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Zoom in"
                    )
                }

                FilledTonalIconButton(onClick = onOpenFilterSort) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Open filter and sort"
                    )
                }
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Search events or Google Places")
                    },
                    singleLine = true
                )

                if (isSearchingGooglePlaces || isCreatingGooglePlaceEvent) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                googlePlacesError?.let { error ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )

                        TextButton(onClick = onDismissGooglePlacesError) {
                            Text("Dismiss")
                        }
                    }
                }

                googlePlaceSuggestions
                    .take(MAX_GOOGLE_PLACE_SUGGESTIONS)
                    .forEach { suggestion ->
                        GooglePlaceSuggestionRow(
                            suggestion = suggestion,
                            enabled = !isCreatingGooglePlaceEvent,
                            onClick = {
                                onGooglePlaceSuggestionClicked(suggestion.placeId)
                            }
                        )
                    }
            }
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
private fun GooglePlaceSuggestionRow(
    suggestion: GooglePlaceSuggestionUi,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = suggestion.primaryText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (suggestion.secondaryText.isNotBlank()) {
                Text(
                    text = suggestion.secondaryText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun GooglePlacePreviewSheet(
    draft: GooglePlaceEventDraft,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onCreateEvent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = draft.location,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        if (draft.address.isNotBlank()) {
            Text(
                text = draft.address,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "Create a custom event from this Google verified venue.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = buildString {
                append("Google verified venue")
                draft.rating?.let { rating ->
                    append(" • Rating: ")
                    append(String.format("%.1f", rating))
                }
                append(" • Price tier: ")
                append(draft.priceTier.name.replace("_", " "))
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Close")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onCreateEvent,
                enabled = !isLoading
            ) {
                Text("Create event")
            }
        }
    }
}

@Composable
private fun GooglePlaceEventDraftSheet(
    draft: GooglePlaceEventDraft,
    isCreating: Boolean,
    onDraftChange: (GooglePlaceEventDraft) -> Unit,
    onDismiss: () -> Unit,
    onCreateEvent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Draft event",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = draft.eventName,
            onValueChange = { value ->
                onDraftChange(
                    draft.copy(eventName = value)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Event name")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = draft.description,
            onValueChange = { value ->
                onDraftChange(
                    draft.copy(description = value)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Description")
            },
            minLines = 2,
            maxLines = 4
        )

        OutlinedTextField(
            value = draft.location,
            onValueChange = { value ->
                onDraftChange(
                    draft.copy(location = value)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Location")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = draft.address,
            onValueChange = { value ->
                onDraftChange(
                    draft.copy(address = value)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Address")
            },
            minLines = 2,
            maxLines = 3
        )

        OutlinedTextField(
            value = draft.startTimeLabel,
            onValueChange = { value ->
                onDraftChange(
                    draft.copy(startTimeLabel = value)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Time label")
            },
            singleLine = true
        )

        Text(
            text = "Category",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        GenreSelectionRows(
            selectedGenre = draft.genre,
            onGenreSelected = { genre ->
                onDraftChange(
                    draft.copy(genre = genre)
                )
            }
        )

        Text(
            text = buildString {
                append("Google verified venue")
                draft.rating?.let { rating ->
                    append(" • Rating: ")
                    append(String.format("%.1f", rating))
                }
                append(" • Price tier: ")
                append(draft.priceTier.name.replace("_", " "))
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onDismiss,
                enabled = !isCreating
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onCreateEvent,
                enabled = !isCreating &&
                        draft.eventName.isNotBlank() &&
                        draft.description.isNotBlank() &&
                        draft.location.isNotBlank() &&
                        draft.address.isNotBlank() &&
                        draft.startTimeLabel.isNotBlank()
            ) {
                Text(
                    text = if (isCreating) {
                        "Creating..."
                    } else {
                        "Save event"
                    }
                )
            }
        }
    }
}

@Composable
private fun GenreSelectionRows(
    selectedGenre: Genre,
    onGenreSelected: (Genre) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Genre.entries.forEach { genre ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onGenreSelected(genre)
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedGenre == genre,
                    onClick = {
                        onGenreSelected(genre)
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = genre.label,
                    modifier = Modifier.weight(1f),
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
                items(
                    items = places,
                    key = { place -> place.id }
                ) { place ->
                    PlaceCard(
                        place = place,
                        selected = place.id == selectedPlaceId,
                        onClick = {
                            onPlaceClicked(place.id)
                        },
                        onSaveClick = {
                            onSaveClick(place.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExploreFilterSortDialog(
    currentGenres: Set<Genre>,
    currentVerifiedOnly: Boolean,
    currentSavedOnly: Boolean,
    currentSortOption: ExploreSortOption,
    onDismiss: () -> Unit,
    onApply: (
        genres: Set<Genre>,
        verifiedOnly: Boolean,
        savedOnly: Boolean,
        sortOption: ExploreSortOption
    ) -> Unit
) {
    var tempGenres by remember(currentGenres) {
        mutableStateOf(currentGenres)
    }

    var tempVerifiedOnly by remember(currentVerifiedOnly) {
        mutableStateOf(currentVerifiedOnly)
    }

    var tempSavedOnly by remember(currentSavedOnly) {
        mutableStateOf(currentSavedOnly)
    }

    var tempSortOption by remember(currentSortOption) {
        mutableStateOf(currentSortOption)
    }

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
                    onCheckedChange = {
                        tempVerifiedOnly = it
                    }
                )

                DialogCheckboxRow(
                    label = "Saved only",
                    checked = tempSavedOnly,
                    onCheckedChange = {
                        tempSavedOnly = it
                    }
                )

                Text(
                    text = "Genres",
                    style = MaterialTheme.typography.titleSmall
                )

                Genre.entries.forEach { genre ->
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
                        onClick = {
                            tempSortOption = option
                        }
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
            .clickable {
                onCheckedChange(!checked)
            }
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
            .clickable {
                onClick()
            }
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

private const val GOOGLE_PREVIEW_PLACE_ID_PREFIX = "google-preview-"

private fun GooglePlaceEventDraft.toPreviewPlaceUi(): PlaceUi {
    return PlaceUi(
        id = toPreviewPlaceId(),
        name = eventName.ifBlank { location },
        subtitle = address.ifBlank { description },
        locationName = location,
        latitude = latitude,
        longitude = longitude,
        distanceKm = null,
        rating = rating ?: 0.0,
        genre = genre,
        isVerified = true,
        isSaved = false,
        savedLabel = null,
        imageUrl = imageUrl
    )
}

private fun GooglePlaceEventDraft.toPreviewPlaceId(): String {
    return "$GOOGLE_PREVIEW_PLACE_ID_PREFIX$googlePlaceId"
}

private fun filterAndSortPlaces(
    places: List<PlaceUi>,
    selectedGenres: Set<Genre>,
    verifiedOnly: Boolean,
    savedOnly: Boolean,
    sortOption: ExploreSortOption
): List<PlaceUi> {
    val filtered = places.filter { place ->
        val genreMatches = selectedGenres.isEmpty() || place.genre in selectedGenres
        val verifiedMatches = !verifiedOnly || place.isVerified
        val savedMatches = !savedOnly || place.isSaved || place.savedLabel != null

        genreMatches && verifiedMatches && savedMatches
    }

    return when (sortOption) {
        ExploreSortOption.FEATURED -> filtered
        ExploreSortOption.DISTANCE -> filtered.sortedBy { place ->
            place.distanceKm ?: Double.MAX_VALUE
        }
        ExploreSortOption.RATING -> filtered.sortedByDescending { place ->
            place.rating
        }
        ExploreSortOption.NAME -> filtered.sortedBy { place ->
            place.name.lowercase()
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