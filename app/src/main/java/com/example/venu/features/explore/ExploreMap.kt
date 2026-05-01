package com.example.venu.features.explore

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.venu.features.explore.model.PlaceUi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.google.maps.model.DirectionsRoute

@Composable
fun ExploreMap(
    modifier: Modifier = Modifier,
    places: List<PlaceUi>,
    selectedPlaceId: String?,
    directionsRoute: DirectionsRoute?,
    onMarkerSelected: (String) -> Unit
) {
    Log.d("DirectionsDebug", "ExploreMap route = $directionsRoute")

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
                16f
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        directionsRoute?.let { route ->
            DrawRoute(route = route)
        }

        places.forEach { place ->
            val markerState = rememberUpdatedMarkerState(
                position = LatLng(place.latitude, place.longitude)
            )

            Marker(
                state = markerState,
                title = place.name,
                snippet = place.subtitle,
                icon = BitmapDescriptorFactory.defaultMarker(
                    if (place.id == selectedPlaceId) {
                        BitmapDescriptorFactory.HUE_AZURE
                    } else {
                        BitmapDescriptorFactory.HUE_RED
                    }
                ),
                onClick = {
                    onMarkerSelected(place.id)
                    false
                }
            )
        }
    }
}

@Composable
fun DrawRoute(route: DirectionsRoute) {
    val pathPoints = remember(route) {
        route.overviewPolyline.decodePath().map {
            LatLng(it.lat, it.lng)
        }
    }

    Polyline(
        points = pathPoints,
        width = 8f
    )
}