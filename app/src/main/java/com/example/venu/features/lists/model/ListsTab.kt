package com.example.venu.features.lists.model

import com.example.venu.core.core_domain.repository.ListType
/*
 * Defining the data shape our ViewModels will expose so our Compose screens
 * can render without the backend.
 *
 * This Model powers our tabbed UI for Want to Go / Already Went / To Review
 */
data class ListsTab(
    val label: String,
    val type: ListType
)

val defaultListsTabs = listOf(
    ListsTab("Want to Go", ListType.WantToGo),
    ListsTab("Already Went", ListType.AlreadyWent),
    ListsTab("To Review", ListType.ToReview),
    ListsTab("Custom List", ListType.Custom("customlist","Custom List"))
)