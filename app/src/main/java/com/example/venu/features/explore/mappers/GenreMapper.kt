package com.example.venu.features.explore.mappers

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.features.explore.ExploreGenre

fun Genre.toExploreGenre(): ExploreGenre = when (this) {
    Genre.FOOD -> ExploreGenre.Food
    Genre.STUDY -> ExploreGenre.Study
    Genre.MUSIC -> ExploreGenre.Music
    Genre.SPORTS -> ExploreGenre.Sports
    Genre.MUSEUMS -> ExploreGenre.Museums
    Genre.COFFEE -> ExploreGenre.Coffee
    Genre.NIGHTLIFE -> ExploreGenre.Nightlife
    Genre.OUTDOORS -> ExploreGenre.Outdoors
}