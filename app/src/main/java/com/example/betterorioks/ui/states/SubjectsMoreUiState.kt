package com.example.betterorioks.ui.states

import com.example.betterorioks.model.SubjectMore

sealed interface SubjectsMoreUiState {
    object Loading: SubjectsMoreUiState
    object Error: SubjectsMoreUiState
    data class Success (val Disciplines:List<SubjectMore>):SubjectsMoreUiState
}