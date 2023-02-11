package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.ImportantDates

sealed interface ImportantDatesUiState {
    object NotStarted: ImportantDatesUiState
    object Loading: ImportantDatesUiState
    object Error: ImportantDatesUiState
    data class Success (val dates: ImportantDates): ImportantDatesUiState
}