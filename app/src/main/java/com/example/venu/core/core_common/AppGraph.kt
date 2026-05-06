package com.example.venu.core.core_common

import android.content.Context
import com.example.venu.BuildConfig
import com.example.venu.core.core_data.local.db.VenuLocalDatabase
import com.example.venu.core.core_data.places.GooglePlacesRepository
import com.example.venu.core.core_data.remote.firestore.EventFirestoreRepository
import com.example.venu.core.core_data.remote.firestore.ReviewFireStoreRepository
import com.example.venu.core.core_data.repository.FirestoreListsRepository
import com.example.venu.core.core_data.repository.RoomEventRepository
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.core.core_domain.repository.ReviewRepository

// Tiny TEMPORARY dependency container so screens can access repos.
// Use this in ViewModels.
object AppGraph {

    private lateinit var database: VenuLocalDatabase

    lateinit var eventRepo: EventRepository
        private set

//    lateinit var eventFirestoreRepo: EventFirestoreRepository
//        private set

    lateinit var listsRepo: ListsRepository
        private set

    lateinit var googlePlacesRepo: GooglePlacesRepository
        private set

    val reviewRepo: ReviewRepository by lazy {
        ReviewFireStoreRepository()
    }

    suspend fun initialize(context: Context) {
        database = VenuLocalDatabase.getDatabase(context)
        val firestoreEventRepository = EventFirestoreRepository()
        val roomEventRepository = RoomEventRepository(database.eventDao())

        // database.clearAllTables()
        // Uncomment only if we want to test reseeding again.

        // roomEventRepository.seedIfEmpty()


        eventRepo = firestoreEventRepository
        val events = eventRepo.getNearbyEvents()
        println("Firebase test events: $events")
        println("AppGraph init: Loaded ${events.size} events from Firebase")
//        eventFirestoreRepo = firestoreEventRepository

        listsRepo = FirestoreListsRepository(
            eventRepo = eventRepo
        )

        googlePlacesRepo = GooglePlacesRepository(
            context = context,
            apiKey = BuildConfig.MAPS_API_KEY
        )
    }
}