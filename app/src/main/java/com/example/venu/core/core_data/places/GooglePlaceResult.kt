package com.example.venu.core.core_data.places
import com.example.venu.core.core_domain.model.PriceTier

data class GooglePlaceResult(
    val placeId: String,
    val name: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val rating: Double?,
    val photoUrl: String?,
    val hours: String?,
    val priceTier: PriceTier
)