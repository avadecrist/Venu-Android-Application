package com.example.venu.core.core_domain.repository

// INTERFACE FOR METHODS
import com.example.venu.core.core_domain.model.Event


sealed class ListType {
    data object WantToGo : ListType()
    data object AlreadyWent : ListType()
    data object ToReview : ListType()

    data class Custom(val id: String, val name: String) : ListType()
    /* example usage:
     *  val customList = ListType.Custom("date_night", "Date Night")
     *  listsRepository.addToList(customList, eventId)
     */
}


interface ListsRepository {
    fun getList(type: ListType): List<Event>
    fun addToList(type: ListType, eventId: String)
    fun removeFromList(type: ListType, eventId: String)
    fun moveEvent(eventId: String, from: ListType, to: ListType)

    fun isInList(type: ListType, eventId: String): Boolean // so cards can show “saved” state to specific list
    fun isSaved(eventId: String): Boolean // if a list is saved in general

    fun toggleWantToGo(eventId: String) // quick way to save to "Want to go" list

    // to edit custom lists:
    fun createCustomList(name: String): ListType.Custom
    fun deleteCustomList(listId: String)
    fun getAllLists(): List<ListType>
}

