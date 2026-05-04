package com.example.venu.core.core_data.remote.firestore

data class ReviewFireStoreDto(
    val reviewId: String = "",
    val eventId: String = "",
    val googlePlaceId: String? = null,
    val uid: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val rating: Int = 0,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis() // can convert to Firestore Timestamp if needed
)