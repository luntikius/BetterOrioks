package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.schedule.Schedule

sealed interface ScheduleUiState {
    object NotStarted : ScheduleUiState
    object Loading : ScheduleUiState
    object Error : ScheduleUiState
    data class Success(val schedule: List<Schedule>) : ScheduleUiState
}