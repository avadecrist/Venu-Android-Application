package com.example.venu.core.core_data.repository

import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_domain.repository.ListsRepository

class InMemoryListsRepository(
    private val eventRepo: EventRepository
) : ListsRepository {

    private val want = LinkedHashSet<String>()
    private val went = LinkedHashSet<String>()
    private val toReview = LinkedHashSet<String>()

    private val customLists = linkedMapOf<String, LinkedHashSet<String>>()
    private val customListNames = linkedMapOf<String, String>()

    override suspend fun getList(type: ListType): List<Event> {
        val ids = when (type) {
            ListType.WantToGo -> want
            ListType.AlreadyWent -> went
            ListType.ToReview -> toReview
            is ListType.Custom -> customLists[type.id] ?: linkedSetOf()
        }

        return ids.mapNotNull { eventRepo.getEventById(it) }
    }

    override suspend fun addToList(type: ListType, eventId: String) {
        setFor(type).add(eventId)
    }

    override suspend fun removeFromList(type: ListType, eventId: String) {
        setFor(type).remove(eventId)
    }

    override suspend fun moveEvent(eventId: String, from: ListType, to: ListType) {
        setFor(from).remove(eventId)
        setFor(to).add(eventId)
    }

    override suspend fun isInList(type: ListType, eventId: String): Boolean {
        return setFor(type).contains(eventId)
    }

    override suspend fun isSaved(eventId: String): Boolean {
        return want.contains(eventId) ||
                went.contains(eventId) ||
                toReview.contains(eventId) ||
                customLists.values.any { it.contains(eventId) }
    }

    override suspend fun toggleWantToGo(eventId: String) {
        if (want.contains(eventId)) {
            want.remove(eventId)
        } else {
            want.add(eventId)
        }
    }

    override suspend fun createCustomList(name: String): ListType.Custom {
        val id = name
            .trim()
            .lowercase()
            .replace("\\s+".toRegex(), "_")

        customListNames[id] = name
        customLists.putIfAbsent(id, linkedSetOf())

        return ListType.Custom(id, name)
    }

    override suspend fun deleteCustomList(listId: String) {
        customLists.remove(listId)
        customListNames.remove(listId)
    }

    override suspend fun getAllLists(): List<ListType> {
        val builtIns = listOf(
            ListType.WantToGo,
            ListType.AlreadyWent,
            ListType.ToReview
        )

        val customs = customListNames.map { (id, name) ->
            ListType.Custom(id = id, name = name)
        }

        return builtIns + customs
    }

    private fun setFor(type: ListType): MutableSet<String> {
        return when (type) {
            ListType.WantToGo -> want
            ListType.AlreadyWent -> went
            ListType.ToReview -> toReview
            is ListType.Custom -> customLists.getOrPut(type.id) { linkedSetOf() }
        }
    }
}