package com.example.betterorioks.ui.states

import com.example.betterorioks.model.TimeTableElement


sealed interface TimeTableUiState{
    object NotStarted: TimeTableUiState
    object Loading: TimeTableUiState
    object Error: TimeTableUiState
    data class Success (val timeTable : TimeTableElement): TimeTableUiState
}