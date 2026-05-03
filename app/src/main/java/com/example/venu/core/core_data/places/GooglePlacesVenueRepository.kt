package com.example.venu.core.core_data.places

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import kotlinx.coroutines.tasks.await

data class GooglePlaceVenue(
    val googlePlaceId: String,
    val venueName: String,
    val venueAddress: String?,
    val latitude: Double,
    val longitude: Double,
    val rating: Double?,
    val photoUrl: String?
)

class GooglePlacesVenueRepository(
    context: Context
) {
    private val placesClient = Places.createClient(context.applicationContext)

    suspend fun getVenueByPlaceId(placeId: String): GooglePlaceVenue {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.LOCATION,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS
        )

        val request = FetchPlaceRequest.newInstance(placeId, fields)
        val response = placesClient.fetchPlace(request).await()
        val place = response.place

        val location: LatLng = requireNotNull(place.location) {
            "Google Places returned no coordinates for placeId=$placeId"
        }

        val firstPhotoMetadata = place.photoMetadatas?.firstOrNull()

        val photoUrl = firstPhotoMetadata?.let { metadata ->
            val photoRequest = FetchResolvedPhotoUriRequest
                .builder(metadata)
                .setMaxWidth(900)
                .setMaxHeight(600)
                .build()

            placesClient
                .fetchResolvedPhotoUri(photoRequest)
                .await()
                .uri
                .toString()
        }

        return GooglePlaceVenue(
            googlePlaceId = place.id ?: placeId,
            venueName = place.displayName ?: "Unknown venue",
            venueAddress = place.formattedAddress,
            latitude = location.latitude,
            longitude = location.longitude,
            rating = place.rating,
            photoUrl = photoUrl
        )
    }
}