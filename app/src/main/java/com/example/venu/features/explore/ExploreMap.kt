package com.example.venu.features.explore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.venu.core.core_presentation.PlaceUi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ExploreMap(
    modifier: Modifier = Modifier,
    places: List<PlaceUi>,
    selectedPlaceId: String?,
    onMarkerSelected: (String) -> Unit
) {
    val defaultCenter = places.firstOrNull()?.let {
        LatLng(it.latitude, it.longitude)
    } ?: LatLng(40.4168, -3.7038)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultCenter, 12f)
    }

    LaunchedEffect(selectedPlaceId, places) {
        val selected = places.firstOrNull { it.id == selectedPlaceId } ?: return@LaunchedEffect
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(selected.latitude, selected.longitude),
                14f
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        places.forEach { place ->
            Marker(
                state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                title = place.name,
                snippet = place.subtitle,
                onClick = {
                    onMarkerSelected(place.id)
                    false
                }
            )
        }
    }
}