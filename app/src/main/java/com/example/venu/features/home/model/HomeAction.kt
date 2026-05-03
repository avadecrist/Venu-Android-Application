package com.example.venu.features.home.model

import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_presentation.EventDetailsUi

sealed interface HomeAction {
    data class QueryChanged(val text: String) : HomeAction

    data class SaveClicked(val eventId: String) : HomeAction

    data class SaveToList(val listType: ListType, val eventId: String) : HomeAction

//    for these ones, it should just navigate to explore and explore's GetDirectionsClicked will handle the rest
//    data class GetDirectionsClicked(val event: EventDetailsUi) : HomeAction
    object DismissSaveSheet : HomeAction
}