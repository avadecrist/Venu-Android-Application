package com.example.venu.core.core_domain.repository

import com.example.venu.core.core_domain.model.Event

sealed class ListType {
    data object WantToGo : ListType()
    data object AlreadyWent : ListType()
    data object ToReview : ListType()

    data class Custom(val id: String, val name: String) : ListType()
}

interface ListsRepository {
    suspend fun getList(type: ListType): List<Event>

    suspend fun addToList(type: ListType, eventId: String)
    suspend fun removeFromList(type: ListType, eventId: String)
    suspend fun moveEvent(eventId: String, from: ListType, to: ListType)

    suspend fun isInList(type: ListType, eventId: String): Boolean
    suspend fun isSaved(eventId: String): Boolean

    suspend fun toggleWantToGo(eventId: String)

    suspend fun createCustomList(name: String): ListType.Custom
    suspend fun deleteCustomList(listId: String)

    suspend fun getAllLists(): List<ListType>
}