package com.example.betterorioks.ui

import com.example.betterorioks.model.Subject
import com.example.betterorioks.ui.states.SubjectsMoreUiState
import com.example.betterorioks.ui.states.SubjectsUiState

data class AppUiState (
    var currentSubject: Subject = Subject(),
    var subjectsUiState: SubjectsUiState = SubjectsUiState.Loading,
    var currentSubjectDisciplines: Map<Int,SubjectsMoreUiState> = mapOf(),
    var isAcademicPerformanceRefreshing: Boolean = false
    )