package com.studentapp.betterorioks.ui

import com.studentapp.betterorioks.model.subjectsFromSite.ControlEvent
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectFromSite
import com.studentapp.betterorioks.ui.states.*
import java.time.LocalDate

data class AppUiState (
    //temp
    var updateState: Boolean = false,
    //const
    var sendNotifications: Boolean = false,
    var sendNewsNotifications: Boolean = false,
    var currentSubject: SubjectFromSite = SubjectFromSite(),
    var currentControlEvent: ControlEvent = ControlEvent(),
    var cookiesErrorCount: Int = 0,
    var subjectsFromSiteUiState: SubjectsFromSiteUiState = SubjectsFromSiteUiState.NotStarted,
    var token: String = "",
    var authCookies: String = "",
    var csrf: String = "",
    var authState: AuthState = AuthState.NotLoggedIn,
    var userInfoUiState: UserInfoUiState = UserInfoUiState.NotStarted,
    var importantDatesUiState: ImportantDatesUiState = ImportantDatesUiState.NotStarted,
    var currentSelectedDate: LocalDate = LocalDate.now(),
    var fullScheduleUiState: FullScheduleUiState = FullScheduleUiState.NotStarted,
    var scheduleInitUiState: Boolean = false,
    val resourcesUiState: ResourcesUiState = ResourcesUiState.NotStarted,
    val newsUiState: NewsUiState = NewsUiState.NotStarted,
    val theme: Int = ThemeUiState.SYSTEM,
    val disciplineGrouping: Boolean = true,
    val selectedSemesterId: Int = 0
    )