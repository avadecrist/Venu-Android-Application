package com.example.venu.core.core_common

// tiny TEMPORARY dependency container so screens can access repos
// use this in ViewModels!

import android.content.Context
import com.example.venu.core.core_data.local.db.VenuLocalDatabase
import com.example.venu.core.core_data.repository.FakeReviewRepository
import com.example.venu.core.core_data.repository.InMemoryListsRepository
import com.example.venu.core.core_data.repository.RoomEventRepository
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.core.core_domain.repository.ReviewRepository

object AppGraph {

    private lateinit var database: VenuLocalDatabase

    lateinit var eventRepo: EventRepository
        private set

    lateinit var listsRepo: ListsRepository
        private set

    val reviewRepo: ReviewRepository by lazy {
        FakeReviewRepository()
    }

    suspend fun initialize(context: Context) {
        database = VenuLocalDatabase.getDatabase(context)

        val roomEventRepository = RoomEventRepository(database.eventDao())
        roomEventRepository.seedIfEmpty()

        // Temp test to confirm Room is initialized
        val events = roomEventRepository.getAllEvents()
        println("AppGraph init: Loaded ${events.size} events from Room")

        eventRepo = roomEventRepository
        listsRepo = InMemoryListsRepository(eventRepo)
    }
}