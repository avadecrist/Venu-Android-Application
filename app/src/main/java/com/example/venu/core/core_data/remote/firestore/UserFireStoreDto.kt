package com.example.venu.core.core_data.remote.firestore

data class UserFirestoreDto(
    val uid: String = "",
    val email: String = "",
    val displayName: String? = null,
    val photoUrl: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)