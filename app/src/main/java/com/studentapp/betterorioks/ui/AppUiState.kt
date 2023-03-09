package com.studentapp.betterorioks.ui

import com.studentapp.betterorioks.model.subjectsFromSite.ControlEvent
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectFromSite
import com.studentapp.betterorioks.ui.states.*
import java.time.LocalDate

data class AppUiState (
    var currentSubject: SubjectFromSite = SubjectFromSite(),
    var currentControlEvent: ControlEvent = ControlEvent(),
    var subjectsFromSiteUiState: SubjectsFromSiteUiState = SubjectsFromSiteUiState.NotStarted,
    var token: String = "",
    var authCookies: String = "",
    var authState: AuthState = AuthState.NotLoggedIn,
    var userInfoUiState: UserInfoUiState = UserInfoUiState.NotStarted,
    var academicDebtsUiState: DebtsUiState = DebtsUiState.NotStarted,
    var importantDatesUiState: ImportantDatesUiState = ImportantDatesUiState.NotStarted,
    var currentSelectedDate: LocalDate = LocalDate.now(),
    var fullScheduleUiState: FullScheduleUiState = FullScheduleUiState.NotStarted,
    var scheduleInitUiState: Boolean = false,
    )