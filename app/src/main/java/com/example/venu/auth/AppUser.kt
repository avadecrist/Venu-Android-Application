package com.example.venu.auth

data class AppUser(
    val idToken: String,
    val email: String?,
    val displayName: String?,
    val profilePictureUri: String?
)