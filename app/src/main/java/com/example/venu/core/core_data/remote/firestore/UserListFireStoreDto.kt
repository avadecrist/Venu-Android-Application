package com.example.venu.core.core_data.remote.firestore

data class UserListFirestoreDto(
    val id: String = "",
    val name: String = "",
    val type: String = "custom",
    val ownerId: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val itemCount: Int = 0
)