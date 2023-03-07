package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.subjectsFromSite.SubjectsData

sealed interface SubjectsFromSiteUiState{
    object NotStarted: SubjectsFromSiteUiState
    object Loading: SubjectsFromSiteUiState
    object Error: SubjectsFromSiteUiState
    data class Success (val subjects:SubjectsData): SubjectsFromSiteUiState
}