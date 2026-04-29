package com.example.venu.core.core_presentation

data class ReviewUi(
    val id: String,
    val authorName: String,
    val authorInitial: String,
    val rating: Int,
    val comment: String,
    val timeAgo: String
)