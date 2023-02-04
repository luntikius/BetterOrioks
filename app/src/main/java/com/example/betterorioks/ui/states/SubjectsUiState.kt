package com.example.betterorioks.ui.states

import com.example.betterorioks.model.Subject

sealed interface SubjectsUiState {
    object Loading: SubjectsUiState
    object Error: SubjectsUiState
    data class Success (val subjects:List<Subject>):SubjectsUiState
}