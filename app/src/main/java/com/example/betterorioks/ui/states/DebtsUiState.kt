package com.example.betterorioks.ui.states

import com.example.betterorioks.model.AcademicDebt

sealed interface DebtsUiState {
    object NotStarted: DebtsUiState
    object Loading: DebtsUiState
    object Error: DebtsUiState
    data class Success (val debts: List<AcademicDebt>): DebtsUiState
}