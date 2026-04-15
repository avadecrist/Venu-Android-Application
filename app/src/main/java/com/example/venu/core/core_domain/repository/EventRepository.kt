package com.example.venu.core.core_domain.repository

// INTERFACE FOR METHODS
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.Genre

interface EventRepository {
    suspend fun getTrendingEvents(): List<Event>
    suspend fun getNearbyEvents(): List<Event> // returns Explore page's default list
    suspend fun getEventsByCategory(genre: Genre): List<Event>
    suspend fun searchEvents(query: String, categories: Set<Genre> = emptySet()): List<Event>

    suspend fun getEventById(id: String): Event?
}