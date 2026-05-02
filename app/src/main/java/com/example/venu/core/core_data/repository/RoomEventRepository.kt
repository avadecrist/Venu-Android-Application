package com.example.venu.core.core_data.repository

import com.example.venu.core.core_data.fake.FakeSeed
import com.example.venu.core.core_data.local.db.dao.EventDao
import com.example.venu.core.core_data.mapper.toDomain
import com.example.venu.core.core_data.mapper.toEntity
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.repository.EventRepository

class RoomEventRepository(
    private val eventDao: EventDao
) : EventRepository {

    suspend fun seedIfEmpty() {
        val countBefore = eventDao.getCount()

        if (countBefore == 0) {
            val seedEvents = FakeSeed.events.map { it.toEntity() }
            eventDao.insertEvents(seedEvents)

            println("VENU DATA DEBUG: Seeded ${seedEvents.size} fake events into Room.")
        } else {
            println("VENU DATA DEBUG: Skipping seed. Room already has $countBefore events.")
        }

        val countAfter = eventDao.getCount()
        println("VENU DATA DEBUG: Room events after seed check = $countAfter")
    }

    // Use for development only
    suspend fun getAllEvents(): List<Event> {
        val events = eventDao.getAllEvents().map { it.toDomain() }

        // log to confirm Room is being read
        println("Loaded ${events.size} events from Room")

        return events
    }

    suspend fun addUserCreatedEvent(event: Event) {
        eventDao.insertEvent(event.toEntity())

        val countAfter = eventDao.getCount()
        println("VENU DATA DEBUG: Created real/user event. Room events now = $countAfter")
    }

    override suspend fun getTrendingEvents(): List<Event> {
        return eventDao.getTrendingEvents().map { it.toDomain() }
    }

    override suspend fun getNearbyEvents(): List<Event> {
        return eventDao.getAllEvents().map { it.toDomain() }
    }

    override suspend fun getEventsByCategory(genre: Genre): List<Event> {
        return eventDao.getEventsByGenre(genre.name).map { it.toDomain() }
    }

    override suspend fun searchEvents(
        query: String,
        categories: Set<Genre>
    ): List<Event> {
        val events = if (query.isBlank()) {
            eventDao.getAllEvents().map { it.toDomain() }
        } else {
            eventDao.searchEvents(query).map { it.toDomain() }
        }

        return if (categories.isEmpty()) {
            events
        } else {
            events.filter { it.genre in categories }
        }
    }

    override suspend fun getEventById(id: String): Event? {
        return eventDao.getEventById(id)?.toDomain()
    }

    override suspend fun createEvent(event: Event) {
        eventDao.insertEvent(event.toEntity())

        val countAfterInsert = eventDao.getCount()
        println("VENU DATA DEBUG: Created real/user event. Room events now = $countAfterInsert")
    }
}