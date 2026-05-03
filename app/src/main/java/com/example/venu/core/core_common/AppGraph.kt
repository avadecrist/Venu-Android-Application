package com.example.venu.core.core_common

// tiny TEMPORARY dependency container so screens can access repos
// use this in ViewModels!

import android.content.Context
import com.example.venu.core.core_data.local.db.VenuLocalDatabase
import com.example.venu.core.core_data.repository.FakeReviewRepository
import com.example.venu.core.core_data.repository.InMemoryListsRepository
import com.example.venu.core.core_data.repository.RoomEventRepository
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier
import com.example.venu.core.core_domain.model.UserCreatedEventFactory
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.core.core_domain.repository.ReviewRepository
import com.example.venu.BuildConfig
import com.example.venu.core.core_data.places.GooglePlacesVenueRepository
import com.google.android.libraries.places.api.Places

object AppGraph {
    private lateinit var database: VenuLocalDatabase

    lateinit var eventRepo: EventRepository
        private set

    lateinit var listsRepo: ListsRepository
        private set

    lateinit var googlePlacesVenueRepository: GooglePlacesVenueRepository
        private set

    val reviewRepo: ReviewRepository by lazy { FakeReviewRepository() }

    suspend fun initialize(context: Context) {
        val appContext = context.applicationContext

        if (!Places.isInitialized()) {
            Places.initialize(appContext, BuildConfig.MAPS_API_KEY)
        }

        database = VenuLocalDatabase.getDatabase(appContext)

        val roomEventRepository = RoomEventRepository(database.eventDao())

        // Keep this for now while we are still using seeded + user-created Room events.
        // Later, once real Google Places flows are complete, this can be removed or made dev-only.
        roomEventRepository.seedIfEmpty()

        val events = roomEventRepository.getAllEvents()
        println("AppGraph init: Loaded ${events.size} events from Room")

        eventRepo = roomEventRepository
        listsRepo = InMemoryListsRepository(eventRepo)
        googlePlacesVenueRepository = GooglePlacesVenueRepository(appContext)
    }
}