package com.example.venu.core.core_data.location

import android.util.Log
import com.example.venu.BuildConfig
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.DirectionsRoute
import com.google.maps.model.TravelMode
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DirectionsService {

    suspend fun getDrivingRoute(
        originLat: Double,
        originLng: Double,
        destinationLat: Double,
        destinationLng: Double
    ): DirectionsRoute {
        val geoContext = GeoApiContext.Builder()
            .apiKey(BuildConfig.MAPS_API_KEY)
            .build()

        val origin = "$originLat,$originLng"
        val destination = "$destinationLat,$destinationLng"

        Log.d("DirectionsDebug", "Origin = $origin")
        Log.d("DirectionsDebug", "Destination = $destination")
        Log.d("DirectionsDebug", "Before DirectionsApi.setCallback()")

        return suspendCancellableCoroutine { continuation ->
            DirectionsApi.newRequest(geoContext)
                .mode(TravelMode.DRIVING)
                .origin(origin)
                .destination(destination)
                .alternatives(false)
                .setCallback(object : PendingResult.Callback<DirectionsResult> {

                    override fun onResult(result: DirectionsResult) {
                        Log.d("DirectionsDebug", "Routes count = ${result.routes.size}")

                        val route = result.routes.firstOrNull()

                        if (route != null) {
                            continuation.resume(route)
                        } else {
                            continuation.resumeWithException(
                                NoSuchElementException("No routes found")
                            )
                        }

                        geoContext.shutdown()
                    }

                    override fun onFailure(e: Throwable) {
                        Log.e("DirectionsDebug", "Directions failed", e)

                        continuation.resumeWithException(e)
                        geoContext.shutdown()
                    }
                })

            continuation.invokeOnCancellation {
                geoContext.shutdown()
            }
        }
    }
}