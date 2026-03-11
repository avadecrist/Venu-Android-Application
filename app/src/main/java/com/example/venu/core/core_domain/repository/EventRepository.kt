package com.example.venu.core.core_domain.repository

// INTERFACE FOR METHODS
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.Genre

interface EventRepository {
    fun getTrendingEvents(): List<Event>
    fun getNearbyEvents(): List<Event> // returns Explore page's default list
    fun getEventsByCategory(genre: Genre): List<Event>
    fun searchEvents(query: String, categories: Set<Genre> = emptySet()): List<Event>

    fun getEventById(id: String): Event?
}