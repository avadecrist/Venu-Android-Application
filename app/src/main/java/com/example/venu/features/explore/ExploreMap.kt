package com.example.venu.features.explore

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.venu.features.explore.model.PlaceUi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

private val MadridLatLng = LatLng(40.4168, -3.7038)

@Composable
fun ExploreMap(
    modifier: Modifier = Modifier,
    places: List<PlaceUi>,
    selectedPlaceId: String?,
    hasLocationPermission: Boolean,
    zoomRequest: Int,
    zoomDelta: Float,
    onMarkerSelected: (String) -> Unit
) {
    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var userLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    val fallbackCenter = places
        .firstOrNull()
        ?.let { place -> LatLng(place.latitude, place.longitude) }
        ?: MadridLatLng

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(fallbackCenter, 12f)
    }

    LaunchedEffect(zoomRequest) {
        if (zoomRequest > 0) {
            val currentPosition = cameraPositionState.position
            val newZoom = (currentPosition.zoom + zoomDelta)
                .coerceIn(3f, 20f)

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    currentPosition.target,
                    newZoom
                )
            )
        }
    }

    val locationPermissionGranted =
        hasLocationPermission &&
                (
                        ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                        )

    LaunchedEffect(locationPermissionGranted) {
        if (locationPermissionGranted) {
            fetchUserLocation(
                fusedLocationClient = fusedLocationClient,
                onLocationFound = { latLng ->
                    userLocation = latLng
                },
                onLocationMissing = {
                    userLocation = null
                }
            )
        } else {
            userLocation = null
        }
    }

    LaunchedEffect(userLocation, locationPermissionGranted) {
        val targetLocation = if (locationPermissionGranted && userLocation != null) {
            userLocation
        } else {
            MadridLatLng
        }

        if (targetLocation != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(targetLocation, 14f)
            )
        }
    }

    LaunchedEffect(selectedPlaceId, places) {
        val selected = places.firstOrNull { place ->
            place.id == selectedPlaceId
        } ?: return@LaunchedEffect

        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(selected.latitude, selected.longitude),
                14f
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = locationPermissionGranted
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = locationPermissionGranted,
            zoomControlsEnabled = false
        )
    ) {
        places.forEach { place ->
            val markerState = rememberUpdatedMarkerState(
                position = LatLng(place.latitude, place.longitude)
            )

            Marker(
                state = markerState,
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

@SuppressLint("MissingPermission")
private fun fetchUserLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFound: (LatLng) -> Unit,
    onLocationMissing: () -> Unit
) {
    fusedLocationClient.getCurrentLocation(
        com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
        null
    ).addOnSuccessListener { location ->
        if (location != null) {
            onLocationFound(
                LatLng(location.latitude, location.longitude)
            )
        } else {
            onLocationMissing()
        }
    }.addOnFailureListener {
        onLocationMissing()
    }
}