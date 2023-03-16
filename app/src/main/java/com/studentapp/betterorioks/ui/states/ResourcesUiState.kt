package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.subjectsFromSite.ResourceCategory

sealed interface ResourcesUiState {
    object NotStarted: ResourcesUiState

    object Loading: ResourcesUiState

    object Error: ResourcesUiState

    data class Success (val resourceCategories: List<ResourceCategory>): ResourcesUiState
}