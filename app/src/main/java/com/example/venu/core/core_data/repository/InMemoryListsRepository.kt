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

    override fun getList(type: ListType): List<Event> {
        val ids = when (type) {
            ListType.WANT_TO_GO -> want
            ListType.ALREADY_WENT -> went
            ListType.TO_REVIEW -> toReview
        }
        return ids.mapNotNull { eventRepo.getEventById(it) }
    }

    override fun addToList(type: ListType, eventId: String) {
        setFor(type).add(eventId)
    }

    override fun removeFromList(type: ListType, eventId: String) {
        setFor(type).remove(eventId)
    }

    override fun moveEvent(eventId: String, from: ListType, to: ListType) {
        setFor(from).remove(eventId)
        setFor(to).add(eventId)
    }

    override fun isInList(type: ListType, eventId: String): Boolean {
        return setFor(type).contains(eventId)
    }

    override fun toggleWantToGo(eventId: String) {
        if (want.contains(eventId)) { // If it's already saved, unsave it
            want.remove(eventId)
        } else {
            want.add(eventId) // else, save it
        }
    }

    private fun setFor(type: ListType): MutableSet<String> {
        return when (type) {
            ListType.WANT_TO_GO -> want
            ListType.ALREADY_WENT -> went
            ListType.TO_REVIEW -> toReview
        }
    }
}