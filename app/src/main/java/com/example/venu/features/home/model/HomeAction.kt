package com.example.venu.features.home.model

sealed interface HomeAction {
    data class QueryChanged(val text: String) : HomeAction
    data class ToggleSaved(val eventId: String) : HomeAction
}