package com.example.venu.core.core_data.repository

import com.example.venu.core.core_data.fake.FakeSeed
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.repository.EventRepository

class FakeEventRepository : EventRepository {

    private val events = FakeSeed.events.toMutableList()

    override suspend fun getTrendingEvents(): List<Event> {
        return events
            .sortedWith(
                compareByDescending<Event> { it.venuRating }
                    .thenByDescending { it.reviewCount }
                    .thenByDescending { it.interestLevel }
            )
            .take(10)
    }

    override suspend fun getNearbyEvents(): List<Event> {
        val hasAnyDistance = events.any { it.distanceKm != null }

        return if (hasAnyDistance) {
            events
                .sortedWith(
                    compareBy<Event> { it.distanceKm ?: Double.MAX_VALUE }
                        .thenByDescending { it.venuRating }
                        .thenByDescending { it.interestLevel }
                )
                .take(20)
        } else {
            getTrendingEvents()
        }
    }

    override suspend fun getEventsByCategory(genre: Genre): List<Event> {
        return events.filter { event ->
            event.genre == genre
        }
    }

    override suspend fun searchEvents(
        query: String,
        categories: Set<Genre>
    ): List<Event> {
        val q = query.trim().lowercase()

        return events
            .filter { event ->
                val matchesQuery = q.isBlank() ||
                        event.eventName.lowercase().contains(q) ||
                        event.description.lowercase().contains(q) ||
                        event.locationName.lowercase().contains(q)

                val matchesCategory = categories.isEmpty() || event.genre in categories

                matchesQuery && matchesCategory
            }
            .sortedWith(
                compareByDescending<Event> { it.venuRating }
                    .thenByDescending { it.reviewCount }
                    .thenByDescending { it.interestLevel }
            )
    }

    override suspend fun getEventById(id: String): Event? {
        return events.find { event ->
            event.id == id
        }
    }

    override suspend fun createEvent(event: Event) {
        events.add(event)
    }
}