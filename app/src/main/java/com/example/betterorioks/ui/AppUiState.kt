package com.example.betterorioks.ui

import com.example.betterorioks.model.Subject
import com.example.betterorioks.ui.states.*

data class AppUiState (
    var currentSubject: Subject = Subject(),
    var subjectsUiState: SubjectsUiState = SubjectsUiState.Loading,
    var currentSubjectDisciplines: Map<Int,SubjectsMoreUiState> = mapOf(),
    var isAcademicPerformanceRefreshing: Boolean = false,
    var token: String = "",
    var authState: AuthState = AuthState.NotLoggedIn,
    var loadingState: Boolean = true,
    var userInfoUiState: UserInfoUiState = UserInfoUiState.NotStarted,
    var academicDebtsUiState: DebtsUiState = DebtsUiState.NotStarted
    )