package com.example.venu.core.core_presentation


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.venu.core.core_common.core_ui.theme.VenuColors
import com.example.venu.core.core_domain.model.Genre

@Composable
fun genreColor(genre: Genre): Color {
    return when (genre) {
        Genre.FOOD -> VenuColors.GenreFood
        Genre.STUDY -> VenuColors.GenreStudy
        Genre.MUSIC -> VenuColors.GenreMusic
        Genre.SPORTS -> VenuColors.GenreSports
        Genre.MUSEUMS -> VenuColors.GenreMuseums
        Genre.COFFEE -> VenuColors.GenreCoffee
        Genre.NIGHTLIFE -> VenuColors.GenreNightlife
        Genre.OUTDOORS -> VenuColors.GenreOutdoors
    }
}