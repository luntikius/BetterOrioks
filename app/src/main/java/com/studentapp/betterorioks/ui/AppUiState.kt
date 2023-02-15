package com.studentapp.betterorioks.ui

import com.studentapp.betterorioks.model.Subject
import com.studentapp.betterorioks.model.schedule.Schedule
import com.studentapp.betterorioks.ui.states.*
import java.time.LocalDate

data class AppUiState (
    var currentSubject: Subject = Subject(),
    var subjectsUiState: SubjectsUiState = SubjectsUiState.Loading,
    var currentSubjectDisciplines: Map<Int, SubjectsMoreUiState> = mapOf(),
    var isAcademicPerformanceRefreshing: Boolean = false,
    var token: String = "",
    var authState: AuthState = AuthState.NotLoggedIn,
    var loadingState: Boolean = true,
    var userInfoUiState: UserInfoUiState = UserInfoUiState.NotStarted,
    var academicDebtsUiState: DebtsUiState = DebtsUiState.NotStarted,
    var importantDatesUiState: ImportantDatesUiState = ImportantDatesUiState.NotStarted,
    var timeTableUiState: TimeTableUiState = TimeTableUiState.NotStarted,
    var groupId: Int = 0,
    var currentSelectedDate: LocalDate = LocalDate.now(),
    var scheduleUiState: ScheduleUiState = ScheduleUiState.NotStarted,
    var scheduleList: MutableList<List<Schedule>> = mutableListOf()
    )