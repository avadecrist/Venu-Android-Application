package com.example.venu.core.core_data.places

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.tasks.await

class GooglePlacesRepository(
    private val context: Context,
    private val apiKey: String
) {

    init {
        if (!Places.isInitialized()) {
            Places.initialize(context.applicationContext, apiKey)
        }
    }

    private val placesClient = Places.createClient(context.applicationContext)

    suspend fun searchPlaces(query: String): List<AutocompletePrediction> {
        if (query.isBlank()) return emptyList()

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        val response = placesClient.findAutocompletePredictions(request).await()
        return response.autocompletePredictions
    }

    suspend fun getPlaceDetails(placeId: String): GooglePlaceResult? {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.LOCATION
        )

        val request = FetchPlaceRequest.newInstance(placeId, fields)
        val response = placesClient.fetchPlace(request).await()
        val place = response.place

        val location: LatLng = place.location ?: return null

        return GooglePlaceResult(
            placeId = place.id ?: placeId,
            name = place.displayName ?: "Unknown place",
            address = place.formattedAddress,
            latitude = location.latitude,
            longitude = location.longitude,
            photoUrl = null
        )
    }
}