package com.example.venu.core.core_domain.repository

// INTERFACE FOR METHODS
import com.example.venu.core.core_domain.model.Event

enum class ListType {
    WANT_TO_GO,
    ALREADY_WENT,
    TO_REVIEW
}

interface ListsRepository {
    fun getList(type: ListType): List<Event>
    fun addToList(type: ListType, eventId: String)
    fun removeFromList(type: ListType, eventId: String)
    fun move(eventId: String, from: ListType, to: ListType)

    fun isInList(type: ListType, eventId: String): Boolean // so cards can show “saved” state

    fun toggleWantToGo(eventId: String) // quick way to save to "Want to go" list
}

