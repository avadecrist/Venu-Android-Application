package com.example.venu.core.core_data.places

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.tasks.await

data class GooglePlaceSuggestion(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String
)

class GooglePlacesRepository(
    context: Context,
    apiKey: String
) {

    init {
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(
                context.applicationContext,
                apiKey
            )
        }
    }

    private val placesClient = Places.createClient(context.applicationContext)

    suspend fun searchPlaces(query: String): List<GooglePlaceSuggestion> {
        if (query.isBlank()) return emptyList()

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query.trim())
            .build()

        val response = placesClient.findAutocompletePredictions(request).await()

        return response.autocompletePredictions.map { prediction ->
            GooglePlaceSuggestion(
                placeId = prediction.placeId,
                primaryText = prediction.getPrimaryText(null).toString(),
                secondaryText = prediction.getSecondaryText(null).toString()
            )
        }
    }

    suspend fun getPlaceDetails(placeId: String): GooglePlaceResult? {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.LOCATION,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS
        )

        val request = FetchPlaceRequest.builder(placeId, fields).build()
        val response = placesClient.fetchPlace(request).await()
        val place = response.place

        val location = place.location ?: return null
        val resolvedPhotoUrl = resolvePhotoUrl(place)

        return GooglePlaceResult(
            placeId = place.id ?: placeId,
            name = place.displayName ?: "Unknown place",
            address = place.formattedAddress,
            latitude = location.latitude,
            longitude = location.longitude,
            rating = place.rating,
            photoUrl = resolvedPhotoUrl
        )
    }

    private suspend fun resolvePhotoUrl(place: Place): String? {
        val photoMetadata = place.photoMetadatas?.firstOrNull() ?: return null

        val photoRequest = FetchResolvedPhotoUriRequest.builder(photoMetadata)
            .setMaxWidth(900)
            .setMaxHeight(600)
            .build()

        return runCatching {
            placesClient.fetchResolvedPhotoUri(photoRequest).await().uri.toString()
        }.getOrNull()
    }
}