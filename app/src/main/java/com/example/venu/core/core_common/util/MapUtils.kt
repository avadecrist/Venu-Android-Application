package com.example.venu.core.core_common.util

import android.content.Context
import android.content.Intent
import android.content.ActivityNotFoundException
import androidx.core.net.toUri

//fun openGoogleMapsDirections(
//    context: Context,
//    destinationLat: Double,
//    destinationLng: Double
//) {
//    val googleMapsUri = "google.navigation:q=$destinationLat,$destinationLng&mode=d".toUri()
//
//    val googleMapsIntent = Intent(Intent.ACTION_VIEW, googleMapsUri).apply {
//        setPackage("com.google.android.apps.maps")
//    }
//
//    try {
//        context.startActivity(googleMapsIntent)
//    } catch (_: ActivityNotFoundException) {
//        openDirectionsInBrowser(
//            context = context,
//            destinationLat = destinationLat,
//            destinationLng = destinationLng
//        )
//    }
//}
//
//private fun openDirectionsInBrowser(
//    context: Context,
//    destinationLat: Double,
//    destinationLng: Double
//) {
//    val browserUri =
//        "https://www.google.com/maps/dir/?api=1" +
//                "&destination=$destinationLat,$destinationLng" +
//                "&travelmode=driving"
//
//    val browserIntent = Intent(Intent.ACTION_VIEW, browserUri.toUri())
//    context.startActivity(browserIntent)
//}