package com.example.venu.core.core_data.places

data class GooglePlaceResult(
    val placeId: String,
    val name: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val photoUrl: String? = null
)