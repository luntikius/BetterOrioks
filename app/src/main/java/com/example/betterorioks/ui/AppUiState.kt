package com.example.betterorioks.ui

import com.example.betterorioks.model.Subject
import com.example.betterorioks.ui.states.SubjectsUiState

data class AppUiState (
    var currentSubject: Subject = Subject(),
    var subjectsUiState: SubjectsUiState = SubjectsUiState.Loading
    )