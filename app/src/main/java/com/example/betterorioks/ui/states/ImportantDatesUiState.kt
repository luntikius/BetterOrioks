package com.example.betterorioks.ui.states

import com.example.betterorioks.model.ImportantDates

sealed interface ImportantDatesUiState {
    object NotStarted: ImportantDatesUiState
    object Loading: ImportantDatesUiState
    object Error: ImportantDatesUiState
    data class Success (val dates: ImportantDates): ImportantDatesUiState
}