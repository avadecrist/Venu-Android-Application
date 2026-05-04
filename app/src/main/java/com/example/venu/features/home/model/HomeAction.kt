package com.example.venu.features.home.model

import com.example.venu.core.core_domain.repository.ListType

sealed interface HomeAction {
    data class QueryChanged(val text: String) : HomeAction

    data class SaveClicked(val eventId: String) : HomeAction

    data class SaveToList(val listType: ListType, val eventId: String) : HomeAction

    object DismissSaveSheet : HomeAction
}