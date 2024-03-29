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
import androidx.navigation.NavController
import com.studentapp.betterorioks.BetterOrioksApplication
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.data.AppDetails.BIG_WINDOW_TIME
import com.studentapp.betterorioks.data.AppDetails.SAMPLE_TIME
import com.studentapp.betterorioks.data.NetworkExitRepository
import com.studentapp.betterorioks.data.NetworkMainRepository
import com.studentapp.betterorioks.data.NetworkOrioksRepository
import com.studentapp.betterorioks.data.NetworkTokenRepository
import com.studentapp.betterorioks.data.UserPreferencesRepository
import com.studentapp.betterorioks.data.background.WorkManagerBetterOrioksRepository
import com.studentapp.betterorioks.data.schedule.NetworkScheduleFromSiteRepository
import com.studentapp.betterorioks.data.schedule.ScheduleOfflineRepository
import com.studentapp.betterorioks.data.subjects.SimpleSubjectsOfflineRepository
import com.studentapp.betterorioks.model.ImportantDates
import com.studentapp.betterorioks.model.UserInfo
import com.studentapp.betterorioks.model.scheduleFromSite.FullSchedule
import com.studentapp.betterorioks.model.scheduleFromSite.SimpleScheduleElement
import com.studentapp.betterorioks.model.subjectsFromSite.ControlEvent
import com.studentapp.betterorioks.model.subjectsFromSite.SimpleSubject
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectFromSite
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectsData
import com.studentapp.betterorioks.ui.components.makeStatusNotification
import com.studentapp.betterorioks.ui.screens.dayOfWeekToInt
import com.studentapp.betterorioks.ui.states.AuthState
import com.studentapp.betterorioks.ui.states.FullScheduleUiState
import com.studentapp.betterorioks.ui.states.ImportantDatesUiState
import com.studentapp.betterorioks.ui.states.NewsUiState
import com.studentapp.betterorioks.ui.states.ResourcesUiState
import com.studentapp.betterorioks.ui.states.SubjectsFromSiteUiState
import com.studentapp.betterorioks.ui.states.UserInfoUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.DAYS
import java.util.Base64
import kotlin.math.abs


class BetterOrioksViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val networkScheduleFromSiteRepository: NetworkScheduleFromSiteRepository,
    private val scheduleOfflineRepository: ScheduleOfflineRepository,
    private val orioksRepository: NetworkOrioksRepository,
    private val workManagerBetterOrioksRepository: WorkManagerBetterOrioksRepository,
    private val simpleSubjectsOfflineRepository: SimpleSubjectsOfflineRepository
): ViewModel() {


    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    fun test(context: Context) {
        viewModelScope.launch {
            _uiState.update{ th -> th.copy(disciplineGrouping = !uiState.value.disciplineGrouping) }
            makeStatusNotification(
                message = "Балл за Экзамен изменен: с \"0\" на \"40\"",
                head = "Изменение баллов по предмету Математический анализ",
                context = context,
                link = "https://orioks.miet.ru/main/view-news?id=563")
        }
    }

    fun changeNotificationState(switchValue:Boolean){
        if(switchValue) {
            workManagerBetterOrioksRepository.checkForUpdates(cookies = uiState.value.authCookies)
        } else{
            workManagerBetterOrioksRepository.cancelChecks()
        }
        viewModelScope.launch {
            userPreferencesRepository.setSendNotifications(switchValue)
        }
        _uiState.update { currentState -> currentState.copy(sendNotifications = switchValue) }
    }

    fun changeNewsNotificationState(switchValue:Boolean){
        if(switchValue) {
            workManagerBetterOrioksRepository.CheckForNews(cookies = uiState.value.authCookies)
        } else{
            workManagerBetterOrioksRepository.cancelNewsChecks()
        }
        viewModelScope.launch {
            userPreferencesRepository.setSendNewsNotifications(switchValue)
        }
        _uiState.update { currentState -> currentState.copy(sendNewsNotifications = switchValue) }
    }

    private fun setCookies(cookies: String, csrf: String){
        _uiState.update { currentState -> currentState.copy(authCookies = cookies.ifBlank { uiState.value.authCookies }, csrf = csrf) }
        viewModelScope.launch {
            userPreferencesRepository.setCookies(cookies.ifBlank { uiState.value.authCookies })
            userPreferencesRepository.setCsrf(csrf)
        }
    }
    fun retrieveToken(context: Context, navController: NavController) {
        viewModelScope.launch {
            _uiState.update { currentUiState -> currentUiState.copy(authState = AuthState.Loading) }
            val token = userPreferencesRepository.token.firstOrNull()
            val cookies = userPreferencesRepository.authCookies.firstOrNull()
            val csrf = userPreferencesRepository.csrf.firstOrNull()
            val sendNotifications = userPreferencesRepository.sendNotifications.firstOrNull()
            val sendNewsNotifications = userPreferencesRepository.sendNewsNotifications.firstOrNull()
            val theme = userPreferencesRepository.theme.firstOrNull()
            val disciplineGrouping = userPreferencesRepository.sortDisciplines.firstOrNull()
            if(token != null && cookies != null && csrf != null && sendNewsNotifications != null && sendNotifications != null && theme != null && disciplineGrouping != null) {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        token = token,
                        authCookies = cookies,
                        csrf = csrf,
                        sendNotifications = sendNotifications,
                        theme = theme,
                        sendNewsNotifications = sendNewsNotifications,
                        disciplineGrouping = disciplineGrouping
                    )
                }
                if (uiState.value.token != "" && uiState.value.authCookies != "") {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            authState = AuthState.LoggedIn,
                            updateState = false
                        )
                    }
                }
                else if (uiState.value.token != "" && uiState.value.authCookies == "") {
                    _uiState.update { currentState -> currentState.copy(updateState = true) }
                    exit(context = context,navController = navController)
                }
                else (_uiState.update { currentUiState -> currentUiState.copy(authState = AuthState.NotLoggedIn) })
            }else{
                retrieveToken(context = context, navController = navController)
            }
        }
    }

    fun setTheme(themeId: Int){
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(theme = themeId) }
            userPreferencesRepository.setTheme(themeId)
        }
    }

    fun exit(context: Context, navController: NavController) {
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(authState = AuthState.Loading) }
            val exitRepository = NetworkExitRepository(uiState.value.token)
            try {
                exitRepository.removeToken()
                userPreferencesRepository.dump()
                scheduleOfflineRepository.dump()
                workManagerBetterOrioksRepository.cancelChecks()
                navController.popBackStack()
                _uiState.update { AppUiState(updateState = uiState.value.updateState, authState = AuthState.NotLoggedIn) }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    userPreferencesRepository.dump()
                    scheduleOfflineRepository.dump()
                    navController.popBackStack()
                    _uiState.update { currentState -> currentState.copy(authState = AuthState.NotLoggedIn) }
                }else{
                    _uiState.update { currentState -> currentState.copy(authState = AuthState.LoggedIn) }
                    Toast.makeText(context,context.getString(R.string.wasnt_able_to_delete_your_token), Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                _uiState.update { currentState -> currentState.copy(authState = AuthState.LoggedIn) }
                Toast.makeText(context,context.getString(R.string.wasnt_able_to_delete_your_token), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun checkCookies(){
        if(uiState.value.cookiesErrorCount >= 2) {
            _uiState.update { currentState -> currentState.copy(cookiesErrorCount = 0) }
            getAuthInfo(
                login = userPreferencesRepository.login.first(),
                password = userPreferencesRepository.password.first()
            )
        }
    }

    fun getResources(){
        println("GET_RESOURCES")
        _uiState.update { currentState -> currentState.copy(resourcesUiState = ResourcesUiState.Loading) }
        viewModelScope.launch {
            try {
                val resList = orioksRepository.getResources(
                    cookies = uiState.value.authCookies,
                    scienceId = uiState.value.currentSubject.scienceId,
                    disciplineId = uiState.value.currentSubject.id
                )
                _uiState.update { currentState -> currentState.copy(resourcesUiState = ResourcesUiState.Success(resList)) }
            }catch (e:Throwable){
                Log.d("GET_RESOURCES", e.message.toString())
                _uiState.update { currentState -> currentState.copy(resourcesUiState = ResourcesUiState.Error) }
                checkCookies()
            }
        }
    }

    fun getNews(){
        println("GET_NEWS")
        _uiState.update { currentState -> currentState.copy(newsUiState = NewsUiState.Loading) }
        viewModelScope.launch {
            try {
                val news = orioksRepository.getNews(cookies = uiState.value.authCookies)
                if (news.isNotEmpty())
                    _uiState.update { currentState -> currentState.copy(newsUiState = NewsUiState.Success(news)) }
            }catch (e:Throwable){
                Log.d("GET_NEWS", e.message.toString())
                _uiState.update { currentState -> currentState.copy(newsUiState = NewsUiState.Error) }
                checkCookies()
            }
        }
    }

    private fun parseAcademicPerformance(subjectsData: SubjectsData):List<SimpleSubject>{
        val subjects = subjectsData.subjects
        val result = mutableListOf<SimpleSubject>()
        subjects.forEach {subject ->
            result.add(SimpleSubject(
                name = subject.name,
                systemId = subject.id,
                userScore = subject.grade.fullScore
                )
            )
            val controlForm = subject.formOfControl.name
            subject.getControlEvents().forEach{controlEvent ->
                val finalSubjectShort = if(controlEvent.shortName != "-" && controlEvent.shortName != " "){"(${controlEvent.shortName})"}else{""}
                result.add(
                    SimpleSubject(
                        name = "${controlEvent.type.name.ifBlank{controlForm}} $finalSubjectShort",
                        systemId = subject.id,
                        userScore = controlEvent.grade.score,
                        isSubject = false
                    )
                )
            }
        }
        return result
    }

    fun getAcademicPerformanceFromSite(semesterId: Int? = null) {
        println("GET_ACADEMIC_PERFORMANCE_FROM_SITE")
        _uiState.update { currentState -> currentState.copy(subjectsFromSiteUiState = SubjectsFromSiteUiState.Loading) }
        viewModelScope.launch {
            try {
                val subjects = orioksRepository.getSubjects(
                    cookies = _uiState.value.authCookies,
                    setCookies = {s1,s2 -> setCookies(s1,s2)},
                    semesterId = semesterId
                )
                Log.d("GET_ACADEMIC_PERFORMANCE_FROM_SITE", uiState.value.csrf)

                val position = subjects.semesters.indexOfFirst{it.dateStart == ""}
                val currentSemesterPosition = if(position > 0) position - 1 else 0

                _uiState.update { currentState ->
                    currentState.copy(
                        subjectsFromSiteUiState = SubjectsFromSiteUiState.Success(
                            subjects
                        ),
                        selectedSemesterId = semesterId ?: subjects.semesters[currentSemesterPosition].id
                    )
                }
                if(subjects.subjects.isNotEmpty() && semesterId == null) {
                    val parsed = parseAcademicPerformance(subjects)
                    simpleSubjectsOfflineRepository.insertItems(parsed)
                }
            } catch (e: Throwable) {
                if(e.message == "Auth Error") _uiState.update { currentState -> currentState.copy(cookiesErrorCount = uiState.value.cookiesErrorCount + 1) }
                checkCookies()
                _uiState.update { currentState -> currentState.copy(subjectsFromSiteUiState = SubjectsFromSiteUiState.Error(e.message.toString())) }
            } catch (e: Exception) {
                _uiState.update { currentState -> currentState.copy(subjectsFromSiteUiState = SubjectsFromSiteUiState.Error(e.message.toString())) }
            }
        }
    }

    fun setCurrentSubject(subject: SubjectFromSite) {
        _uiState.update { currentState -> currentState.copy(currentSubject = subject, resourcesUiState = ResourcesUiState.NotStarted) }
    }

    fun setCurrentControlEvent(controlEvent: ControlEvent){
        _uiState.update { currentState -> currentState.copy(currentControlEvent = controlEvent) }
    }

    fun setCurrentDateWithMovingTopBar(date: LocalDate, lazyRowState: LazyListState, coroutineScope: CoroutineScope, startDate: LocalDate) {
        _uiState.update { currentState -> currentState.copy(currentSelectedDate = date) }
        coroutineScope.launch {
            try {
                lazyRowState.animateScrollToItem(
                    abs(
                        DAYS.between(startDate,date).toInt() - dayOfWeekToInt(date)
                    )
                )
            }catch (e: Exception){
                Log.e("InitialisingError",e.toString())
            }
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
                    orioksRepository = application.container.orioksRepository,
                    workManagerBetterOrioksRepository = application.container.workManagerBetterOrioksRepository,
                    simpleSubjectsOfflineRepository = application.container.simpleSubjectsOfflineRepository
                )
            }
        }
    }

    fun getAuthInfo(login: String = "", password: String = "") {
        Log.d("GET_AUTH_INFO","Started")
        _uiState.update { currentState -> currentState.copy(authState = AuthState.Loading) }
        val encodedLoginDetails = Base64.getEncoder().encodeToString("$login:$password".toByteArray())
        val tokenRepository = NetworkTokenRepository(encodedLoginDetails)
        viewModelScope.launch {
            try {
                val token = tokenRepository.getToken()
                val cookies = orioksRepository.auth(login = login, password = password, setCookies = {s1,s2 -> setCookies(s1,s2)})
                if (token.token != "" && cookies != "") {
                    userPreferencesRepository.setCookies(cookies = cookies)
                    userPreferencesRepository.setToken(token = token.token)
                    userPreferencesRepository.setLoginAndPassword(login = login, password = password)
                    _uiState.update { currentState ->
                        currentState.copy(
                            token = token.token,
                            authCookies = cookies,
                            userInfoUiState = UserInfoUiState.NotStarted,
                            subjectsFromSiteUiState = SubjectsFromSiteUiState.NotStarted,
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
            }catch(e: java.net.SocketTimeoutException){
                _uiState.update { currentState -> currentState.copy(authState = AuthState.TimeOut)}
            }
            Log.d("GET_AUTH_INFO","Ended")
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
                val group = userPreferencesRepository.group.firstOrNull()
                val studyDirection = userPreferencesRepository.studyDirection.firstOrNull()
                val studentId = userPreferencesRepository.studentId.firstOrNull()
                val fullName = userPreferencesRepository.fullName.firstOrNull()
                val department = userPreferencesRepository.department.firstOrNull()
                if(group != null && studyDirection != null && studentId != null && fullName != null && department != null) {
                    if (group.isBlank() || refresh) {
                        val userInfo = mainRepository.getUserInfo()
                        _uiState.update { currentState ->
                            currentState.copy(
                                userInfoUiState = UserInfoUiState.Success(
                                    userInfo
                                )
                            )
                        }
                        userPreferencesRepository.setUserInfo(userInfo)
                    }
                    else {
                        _uiState.update { currentState ->
                            currentState.copy(
                                userInfoUiState =
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
                }else{
                    throw java.lang.Exception("Can't get user information")
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
            val semesterStart = userPreferencesRepository.semesterStart.firstOrNull()
            val sessionStart = userPreferencesRepository.sessionStart.firstOrNull()
            if (semesterStart == "" || refresh || semesterStart == null) {
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
                                sessionStart = sessionStart ?: ""
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
                        from = element.time.timeFrom,
                        to = element.time.timeTo,
                        type = element.subject.formFromString,
                        name = element.subject.nameFromString,
                        teacher = element.subject.teacherFull,
                        room = element.room.name
                    )
                )
            }
            for(j in 0..tempData.size-2){
                val timeBetweenPairs = abs(ChronoUnit.MINUTES.between(LocalDateTime.parse(tempData[j+1].time.timeFrom), LocalDateTime.parse(tempData[j].time.timeTo)))
                if(timeBetweenPairs > 10){
                    resultElement.add(
                        SimpleScheduleElement(
                            day = week*7 + day,
                            number = tempData[j].time.dayOrder,
                            isWindow = true,
                            windowDuration = timeBetweenPairs.toString()
                        )
                    )
                }
            }
            result.add(resultElement.sortedBy { it.number })
        }
        return result
    }

    fun recalculateWindows(day: Int, number: Int){
        try {
            viewModelScope.launch {
                Log.d("RECALCULATE_WINDOWS","started")
                val fullSchedule =
                    (uiState.value.fullScheduleUiState as FullScheduleUiState.Success).schedule.toMutableList()
                val week = fullSchedule[day - 1]
                val weekWithoutWindows = week.filter { !it.isWindow }
                weekWithoutWindows.forEach {
                    if (LocalDateTime.parse(it.from) in SAMPLE_TIME && it.number == number) {
                        it.from = LocalDateTime.parse(it.from).plusMinutes(BIG_WINDOW_TIME).toString()
                        it.to = LocalDateTime.parse(it.to).plusMinutes(BIG_WINDOW_TIME).toString()
                    }
                    else if (it.number == number) {
                        it.from = LocalDateTime.parse(it.from).minusMinutes(BIG_WINDOW_TIME).toString()
                        it.to = LocalDateTime.parse(it.to).minusMinutes(BIG_WINDOW_TIME).toString()
                    }
                }
                val resultElement = weekWithoutWindows.toMutableList()
                Log.d("RECALCULATE_WINDOWS",resultElement.toString())
                for (j in 0..weekWithoutWindows.size - 2) {
                    val timeBetweenPairs = abs(
                        ChronoUnit.MINUTES.between(
                            LocalDateTime.parse(weekWithoutWindows[j + 1].from),
                            LocalDateTime.parse(weekWithoutWindows[j].to)
                        )
                    )
                    if (timeBetweenPairs > 10) {
                        resultElement.add(
                            SimpleScheduleElement(
                                day = day,
                                number = weekWithoutWindows[j].number,
                                isWindow = true,
                                windowDuration = timeBetweenPairs.toString()
                            )
                        )
                    }
                }
                fullSchedule[day - 1] = resultElement.sortedBy { it.number }
                _uiState.update { currentState ->
                    currentState.copy(
                        fullScheduleUiState = FullScheduleUiState.Success(
                            fullSchedule
                        )
                    )
                }
                scheduleOfflineRepository.dump()
                for(list in fullSchedule) {
                    for (item in list)
                        scheduleOfflineRepository.insertItem(item)
                }
            }
        }catch(e:Throwable){
            Log.d("RECALCULATE_WINDOWS", e.message.toString())
        }
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
                suspendGetImportantDates(refresh)
                suspendGetUserInfo(refresh)
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
                        val sched = scheduleOfflineRepository.getAllItemsStream(i+1).firstOrNull()
                        if(sched != null) {
                            res.add(
                                sched.sortedBy { it.number })
                        }
                    }
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = FullScheduleUiState.Success(res)) }
                }
                Log.d("TEST", "getFullSchedule ended")
            }catch (e: HttpException){
                Log.d("TEST", e.message.toString())
                if (previousState is FullScheduleUiState.Success) {
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = previousState) }
                    errorToast(context = context)
                }
                else
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = FullScheduleUiState.Error) }
            }catch (e: java.lang.Exception){
                Log.d("TEST", e.message.toString())
                if (previousState is FullScheduleUiState.Success) {
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = previousState) }
                    errorToast(context = context)
                }
                else
                    _uiState.update { currentState -> currentState.copy(fullScheduleUiState = FullScheduleUiState.Error) }
            }
        }
    }

    fun changeSortedState(){
        val currentGroupingState = uiState.value.disciplineGrouping
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(disciplineGrouping = !currentGroupingState) }
            userPreferencesRepository.setSortDisciplines(!currentGroupingState)
        }
    }
}