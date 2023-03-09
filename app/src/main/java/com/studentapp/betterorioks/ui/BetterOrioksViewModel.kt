package com.studentapp.betterorioks.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.studentapp.betterorioks.BetterOrioksApplication
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.data.*
import com.studentapp.betterorioks.data.schedule.NetworkScheduleFromSiteRepository
import com.studentapp.betterorioks.data.schedule.ScheduleOfflineRepository
import com.studentapp.betterorioks.model.*
import com.studentapp.betterorioks.model.scheduleFromSite.FullSchedule
import com.studentapp.betterorioks.model.scheduleFromSite.SimpleScheduleElement
import com.studentapp.betterorioks.model.subjectsFromSite.ControlEvent
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectFromSite
import com.studentapp.betterorioks.ui.screens.dayOfWeekToInt
import java.time.temporal.ChronoUnit.DAYS
import com.studentapp.betterorioks.ui.states.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.util.*
import kotlin.math.abs

class BetterOrioksViewModel(
   // private val academicPerformanceRepository: AcademicPerformanceRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val networkScheduleFromSiteRepository: NetworkScheduleFromSiteRepository,
    private val scheduleOfflineRepository: ScheduleOfflineRepository,
    private val orioksRepository: NetworkOrioksRepository
): ViewModel() {


    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    fun test(){
    }

    private fun setCookies(cookies: String){
        _uiState.update { currentState -> currentState.copy(authCookies = cookies) }
        viewModelScope.launch {
            userPreferencesRepository.setCookies(cookies)
        }
    }
    fun retrieveToken() {
        viewModelScope.launch {
            _uiState.update { currentUiState -> currentUiState.copy(authState = AuthState.Loading) }
            val token = userPreferencesRepository.token.first()
            val cookies = userPreferencesRepository.authCookies.first()
            _uiState.update { currentUiState -> currentUiState.copy(token = token, authCookies = cookies) }
            if (uiState.value.token != "" && uiState.value.authCookies != "") {
                _uiState.update { currentUiState -> currentUiState.copy(authState = AuthState.LoggedIn) }
            }else if(uiState.value.token != "" && uiState.value.authCookies == ""){
                exit()
            }
            else (_uiState.update { currentUiState -> currentUiState.copy(authState = AuthState.NotLoggedIn) })
        }
    }

    fun exit() {
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(authState = AuthState.Loading) }
            val exitRepository = NetworkExitRepository(uiState.value.token)
            try {
                exitRepository.removeToken()
                userPreferencesRepository.dump()
                scheduleOfflineRepository.dump()
                _uiState.update { AppUiState() }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    userPreferencesRepository.setToken("")
                    userPreferencesRepository.setCookies("")
                    _uiState.update { currentState -> currentState.copy(authState = AuthState.NotLoggedIn) }
                }
            } catch (e: IOException) {
                _uiState.update { currentState -> currentState.copy(authState = AuthState.LoggedIn) }
            }
        }
    }

    fun getAcademicPerformanceFromSite() {
        println("GET_ACADEMIC_PERFORMANCE_FROM_SITE")
        _uiState.update { currentState -> currentState.copy(subjectsFromSiteUiState = SubjectsFromSiteUiState.Loading) }
        viewModelScope.launch {
            try {
                val subjects = orioksRepository.getSubjects(
                    cookies = _uiState.value.authCookies,
                    setCookies = { setCookies(it) }
                )
                Log.d("GET_ACADEMIC_PERFORMANCE_FROM_SITE", subjects.toString())
                _uiState.update { currentState -> currentState.copy(subjectsFromSiteUiState = SubjectsFromSiteUiState.Success(subjects)) }
            } catch (e: java.lang.Exception) {
                _uiState.update { currentState -> currentState.copy(subjectsFromSiteUiState = SubjectsFromSiteUiState.Error) }
            } catch (e: HttpException) {
                _uiState.update { currentState -> currentState.copy(subjectsFromSiteUiState = SubjectsFromSiteUiState.Error) }
            }
            suspendGetImportantDates()
        }
    }

    fun setCurrentSubject(subject: SubjectFromSite) {
        _uiState.update { currentState -> currentState.copy(currentSubject = subject) }
    }

    fun setCurrentControlEvent(controlEvent: ControlEvent){
        _uiState.update { currentState -> currentState.copy(currentControlEvent = controlEvent) }
    }

    fun setCurrentDateWithMovingTopBar(date: LocalDate, lazyRowState: LazyListState, coroutineScope: CoroutineScope, startDate: LocalDate) {
        _uiState.update { currentState -> currentState.copy(currentSelectedDate = date) }
        coroutineScope.launch {
            lazyRowState.animateScrollToItem(abs(DAYS.between(startDate,date).toInt() - dayOfWeekToInt(date)))
            if (!uiState.value.scheduleInitUiState) _uiState.update { currentState -> currentState.copy(scheduleInitUiState = true) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BetterOrioksApplication)
                BetterOrioksViewModel(
                    userPreferencesRepository = application.userPreferencesRepository,
                    networkScheduleFromSiteRepository = application.networkScheduleFromSiteRepository,
                    scheduleOfflineRepository = application.container.scheduleRepository,
                    orioksRepository = application.container.orioksRepository
                )
            }
        }
    }

    fun getAuthInfo(login: String = "", password: String = "") {
        println("GET_AUTH_INFO")
        val encodedLoginDetails = Base64.getEncoder().encodeToString("$login:$password".toByteArray())
        val tokenRepository = NetworkTokenRepository(encodedLoginDetails)
        viewModelScope.launch {
            try {
                val token = tokenRepository.getToken()
                val cookies = orioksRepository.auth(login = login, password = password)
                if (token.token != "" && cookies != "") {
                    userPreferencesRepository.setCookies(cookies = cookies)
                    userPreferencesRepository.setToken(token = token.token)
                    _uiState.update { currentState ->
                        currentState.copy(
                            token = token.token,
                            authCookies = cookies,
                            userInfoUiState = UserInfoUiState.NotStarted,
                            academicDebtsUiState = DebtsUiState.NotStarted,
                            subjectsFromSiteUiState = SubjectsFromSiteUiState.Loading,
                            authState = AuthState.LoggedIn
                        )
                    }
                    Log.d("GET_AUTH_INFO", cookies)
                }
            } catch
                (e: HttpException) {
                val error = when (e.code()) {
                    401 -> AuthState.TokenLimitReached
                    403 -> AuthState.BadLoginOrPassword
                    else -> AuthState.UnexpectedError
                }
                _uiState.update { currentState -> currentState.copy(authState = error) }
            }
        }
    }

    fun getUserInfo(refresh: Boolean = false){
        viewModelScope.launch {
            suspendGetUserInfo(refresh = refresh)
        }
    }
    private suspend fun suspendGetUserInfo(refresh: Boolean = false) {
        println("GET_USER_INFO")
        val mainRepository = NetworkMainRepository(uiState.value.token)
        val previousState = uiState.value.userInfoUiState
        _uiState.update { currentState -> currentState.copy(userInfoUiState = UserInfoUiState.Loading) }
            try {
                Log.d("TEST", "getUserInfo started")
                val group = userPreferencesRepository.group.first()
                val studyDirection = userPreferencesRepository.studyDirection.first()
                val studentId = userPreferencesRepository.studentId.first()
                val fullName = userPreferencesRepository.fullName.first()
                val department = userPreferencesRepository.department.first()
                if (group.isBlank() || refresh){
                    val userInfo = mainRepository.getUserInfo()
                    _uiState.update { currentState ->
                        currentState.copy(
                            userInfoUiState = UserInfoUiState.Success(
                                userInfo
                            )
                        )
                    }
                    userPreferencesRepository.setUserInfo(userInfo)
                }else{
                    _uiState.update { currentState ->
                        currentState.copy(userInfoUiState =
                            UserInfoUiState.Success(
                                UserInfo(
                                    group = group,
                                    studyDirection = studyDirection,
                                    recordBookId = studentId,
                                    fullName = fullName,
                                    department = department
                                )
                            )
                        )
                    }
                }
                Log.d("TEST", "getUserInfo ended")
            } catch (e: HttpException) {
                if (previousState is UserInfoUiState.Success) _uiState.update { currentState -> currentState.copy(userInfoUiState = previousState) }
                else _uiState.update { currentState -> currentState.copy(userInfoUiState = UserInfoUiState.Error) }
            } catch (e: java.lang.Exception) {
                if (previousState is UserInfoUiState.Success) _uiState.update { currentState -> currentState.copy(userInfoUiState = previousState) }
                else _uiState.update { currentState -> currentState.copy(userInfoUiState = UserInfoUiState.Error) }
            }
    }

    private suspend fun suspendGetImportantDates(refresh: Boolean = false) {
        println("GET_IMPORTANT_DATES")
        val mainRepository = NetworkMainRepository(uiState.value.token)
        val previousState = uiState.value.importantDatesUiState
        _uiState.update { currentState -> currentState.copy(importantDatesUiState = ImportantDatesUiState.Loading) }
        try {
            val semesterStart = userPreferencesRepository.semesterStart.first()
            val sessionStart = userPreferencesRepository.sessionStart.first()
            if (semesterStart == "" || refresh) {
                val importantDates = mainRepository.getImportantDates()
                _uiState.update { currentState ->
                    currentState.copy(
                        importantDatesUiState = ImportantDatesUiState.Success(
                            importantDates
                        )
                    )
                }
                userPreferencesRepository.setImportantDates((uiState.value.importantDatesUiState as ImportantDatesUiState.Success).dates)
            }
            else {
                _uiState.update { currentState ->
                    currentState.copy(
                        importantDatesUiState = ImportantDatesUiState.Success(
                            ImportantDates(
                                semesterStart = semesterStart,
                                sessionStart = sessionStart
                            )
                        )
                    )
                }
            }
        } catch (e: HttpException) {
            Log.d("GET_IMPORTANT_DATES", e.toString())
            if (previousState is ImportantDatesUiState.Success) _uiState.update { currentState ->
                currentState.copy(
                    importantDatesUiState = previousState
                )
            }
            else _uiState.update { currentState -> currentState.copy(importantDatesUiState = ImportantDatesUiState.Error) }
        } catch (e: java.lang.Exception) {
            if (previousState is ImportantDatesUiState.Success) _uiState.update { currentState ->
                currentState.copy(
                    importantDatesUiState = previousState
                )
            }
            else _uiState.update { currentState -> currentState.copy(importantDatesUiState = ImportantDatesUiState.Error) }
        }
    }

    fun getAcademicDebts() {
        println("GET_ACADEMIC_DEBTS")
        val mainRepository = NetworkMainRepository(uiState.value.token)
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(academicDebtsUiState = DebtsUiState.Loading) }
            try {
                val academicDebts = mainRepository.getAcademicDebt()
                _uiState.update { currentState ->
                    currentState.copy(
                        academicDebtsUiState = DebtsUiState.Success(
                            academicDebts
                        )
                    )
                }
            } catch (e: HttpException) {
                _uiState.update { currentState -> currentState.copy(academicDebtsUiState = DebtsUiState.Error) }
            } catch (e: java.lang.Exception) {
                _uiState.update { currentState -> currentState.copy(academicDebtsUiState = DebtsUiState.Error) }
            }
        }
    }

    fun getTodaysSchedule(date: LocalDate):List<SimpleScheduleElement>{
        return if (uiState.value.importantDatesUiState is ImportantDatesUiState.Success && uiState.value.fullScheduleUiState is FullScheduleUiState.Success) {
            val start =
                LocalDate.parse((uiState.value.importantDatesUiState as ImportantDatesUiState.Success).dates.semesterStart)
            val diff = (DAYS.between(start, date).toInt())
            (uiState.value.fullScheduleUiState as FullScheduleUiState.Success).schedule[abs(diff % 28)]
        }else listOf()
    }

    private fun parseFromFullSchedule(fullSchedule: FullSchedule): List<List<SimpleScheduleElement>>{
        val data = fullSchedule.schedule
        val result = mutableListOf<List<SimpleScheduleElement>>()
        for(i in 1..28){
            val resultElement = mutableListOf<SimpleScheduleElement>()
            val day = i%7
            val week = i/7
            val tempData = data.filter { it.day == day && it.dayNumber == week }.sortedBy { it.time.dayOrder }
            for(element in tempData){
                resultElement.add(
                    SimpleScheduleElement(
                        day = week*7 + day,
                        number = element.time.dayOrder,
                        times = element.time.timeString,
                        type = element.subject.formFromString,
                        name = element.subject.nameFromString,
                        teacher = element.subject.teacherFull,
                        room = element.room.name
                    )
                )
            }
            for(j in 0..tempData.size-2){
                if(tempData[j+1].time.dayOrder - tempData[j].time.dayOrder > 1){
                    resultElement.add(
                        SimpleScheduleElement(
                            day = week*7 + day,
                            number = tempData[j].time.dayOrder+1,
                            isWindow = true,
                            windowDuration = ((tempData[j+1].time.dayOrder - tempData[j].time.dayOrder-1)*1.5).toString()
                        )
                    )
                }
            }
            result.add(resultElement.sortedBy { it.number })
        }
        return result
    }

    private fun errorToast(context: Context){
        Toast.makeText(context, context.getString(R.string.error_while_loading_data), Toast.LENGTH_SHORT).show()
    }

    fun getFullSchedule(refresh: Boolean = false, context: Context){
        println("GET_FULL_SCHEDULE")
        val previousState = uiState.value.fullScheduleUiState
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(fullScheduleUiState = FullScheduleUiState.Loading) }
            try {
                Log.d("TEST", "getFullSchedule started")
                if (uiState.value.importantDatesUiState is ImportantDatesUiState.NotStarted || uiState.value.importantDatesUiState is ImportantDatesUiState.Error) suspendGetImportantDates(refresh)
                if (uiState.value.userInfoUiState is UserInfoUiState.NotStarted || uiState.value.userInfoUiState is UserInfoUiState.Error)suspendGetUserInfo(refresh)
                val res = mutableListOf<List<SimpleScheduleElement>>()
                if(scheduleOfflineRepository.count() == 0 || refresh) {
                    if(refresh) scheduleOfflineRepository.dump()
                    var fullSchedule = FullSchedule()
                    networkScheduleFromSiteRepository.getSchedule((uiState.value.userInfoUiState as UserInfoUiState.Success).userInfo.group) {
                        fullSchedule = it
                    }
                    res.addAll(parseFromFullSchedule(fullSchedule))
                    for(list in res) {
                        for (item in list)
                            scheduleOfflineRepository.insertItem(item)
                    }
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = FullScheduleUiState.Success(res)) }
                }else{
                    for(i in 0..27){
                        res.add(scheduleOfflineRepository.getAllItemsStream(i+1).first())
                    }
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = FullScheduleUiState.Success(res)) }
                }
                Log.d("TEST", "getFullSchedule ended")
            }catch (e: HttpException){
                if (previousState is FullScheduleUiState.Success) {
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = previousState) }
                    errorToast(context = context)
                }
                else
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = FullScheduleUiState.Error) }
            }catch (e: java.lang.Exception){
                if (previousState is FullScheduleUiState.Success) {
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = previousState) }
                    errorToast(context = context)
                }
                else
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = FullScheduleUiState.Error) }
            }
        }
    }
}