package com.example.venu.features.home.model

data class HomeVenueUi(
    val id: String,
    val title: String,
    val subtitle: String,

    val latitude: Double,
    val longitude: Double,

    val ratingLabel: String? = null,
    val distanceLabel: String? = null,
    val isSaved: Boolean = false
)