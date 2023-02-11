package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.schedule.TimeTableElement


sealed interface TimeTableUiState{
    object NotStarted: TimeTableUiState
    object Loading: TimeTableUiState
    object Error: TimeTableUiState
    data class Success (val timeTable : TimeTableElement): TimeTableUiState
}