package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.scheduleFromSite.SimpleScheduleElement

sealed interface FullScheduleUiState {
    object NotStarted : FullScheduleUiState
    object Loading : FullScheduleUiState
    object Error : FullScheduleUiState
    data class Success(val schedule: List<List<SimpleScheduleElement>>) : FullScheduleUiState
}