package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.AcademicDebt

sealed interface DebtsUiState {
    object NotStarted: DebtsUiState
    object Loading: DebtsUiState
    object Error: DebtsUiState
    data class Success (val debts: List<AcademicDebt>): DebtsUiState
}