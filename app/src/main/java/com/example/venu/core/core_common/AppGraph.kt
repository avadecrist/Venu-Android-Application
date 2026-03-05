package com.example.venu.core.core_common

// tiny TEMPORARY dependency container so screens can access repos
// use this in ViewModels!

import com.example.venu.core.core_data.repository.FakeEventRepository
import com.example.venu.core.core_data.repository.FakeReviewRepository
import com.example.venu.core.core_data.repository.InMemoryListsRepository
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ReviewRepository
import com.example.venu.core.core_domain.repository.ListsRepository

object AppGraph {

    val eventRepo: EventRepository by lazy {
        FakeEventRepository() // swap for real Repo later
    }

    val reviewRepo: ReviewRepository by lazy {
        FakeReviewRepository() // swap for real Repo later
    }

    val listsRepo: ListsRepository by lazy {
        InMemoryListsRepository(eventRepo) // swap for real Repo later
    }
}