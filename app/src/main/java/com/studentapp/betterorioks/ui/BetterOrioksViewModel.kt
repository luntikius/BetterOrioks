package com.studentapp.betterorioks.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.studentapp.betterorioks.BetterOrioksApplication
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.BetterOrioksScreens
import com.studentapp.betterorioks.model.Subject
import com.studentapp.betterorioks.model.Token
import com.studentapp.betterorioks.data.*
import com.studentapp.betterorioks.model.schedule.Schedule
import java.time.temporal.ChronoUnit.DAYS
import com.studentapp.betterorioks.ui.states.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.util.*

class BetterOrioksViewModel(
   // private val academicPerformanceRepository: AcademicPerformanceRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {


    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()
    fun retrieveToken(){
        viewModelScope.launch {
            _uiState.update { currentUiState -> currentUiState.copy(authState = AuthState.Loading) }
            val token = userPreferencesRepository.token.first()
            _uiState.update { currentUiState -> currentUiState.copy(token = token) }
            if(uiState.value.token != "") {_uiState.update { currentUiState -> currentUiState.copy(authState = AuthState.LoggedIn) }}
            else(_uiState.update { currentUiState -> currentUiState.copy(authState = AuthState.NotLoggedIn) })
        }
    }

    fun exit(navController: NavController){
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(authState = AuthState.Loading) }
            val exitRepository = NetworkExitRepository(uiState.value.token)
            try{
                exitRepository.removeToken()
                userPreferencesRepository.setToken("")
                _uiState.update { AppUiState() }
                navController.popBackStack(
                    route = BetterOrioksScreens.Schedule.name,
                    inclusive = false
                )
            }catch(e:HttpException) {
                if (e.code() == 401){
                    userPreferencesRepository.setToken("")
                    _uiState.update { currentState -> currentState.copy(authState = AuthState.NotLoggedIn) }
                }
            }catch(e: IOException){
                _uiState.update { currentState -> currentState.copy(authState = AuthState.LoggedIn) }
            }
        }
    }

    fun getAcademicPerformance(){
        println("GET_ACADEMIC_PERFORMANCE")
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(subjectsUiState = SubjectsUiState.Loading, isAcademicPerformanceRefreshing = true, loadingState = false) }
            delay(500)
            val academicPerformanceRepository = NetworkMainRepository(token = uiState.value.token)
            val subjectsUiState = try{
                val subjects = academicPerformanceRepository.getAcademicPerformance()
                SubjectsUiState.Success(subjects)
            }catch (e: java.lang.Exception){
                SubjectsUiState.Error
            }catch (e:HttpException){
                SubjectsUiState.Error
            }
            _uiState.update { currentState -> currentState.copy(subjectsUiState = subjectsUiState, isAcademicPerformanceRefreshing = false)}
            if(uiState.value.subjectsUiState is SubjectsUiState.Success) {
                (uiState.value.subjectsUiState as SubjectsUiState.Success).subjects.forEach{ subject ->
                    getAcademicPerformanceMore(subject.id)
                }
            }
            getImportantDates()
        }
    }

    private fun getAcademicPerformanceMore(disciplineId: Int){
        println("GET_ACADEMIC_PERFORMANCE_MORE")
        val academicPerformanceMoreRepository = NetworkAcademicPerformanceMoreRepository(disciplineId, token = uiState.value.token)
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(currentSubjectDisciplines = _uiState.value.currentSubjectDisciplines + Pair(disciplineId,
                SubjectsMoreUiState.Loading))}
            val subjectsUiState = try{
                val subjects = academicPerformanceMoreRepository.getAcademicPerformanceMore()
                SubjectsMoreUiState.Success(subjects)
            }catch (e: java.lang.Exception){
                SubjectsMoreUiState.Error
            }catch (e:HttpException){
                SubjectsMoreUiState.Error
            }
            _uiState.update { currentState -> currentState.copy(currentSubjectDisciplines = _uiState.value.currentSubjectDisciplines + Pair(disciplineId,subjectsUiState))}
        }
    }

    fun setCurrentSubject(subject: Subject){
        _uiState.update {currentState -> currentState.copy(currentSubject = subject)}
    }

    fun setCurrentDate(date: LocalDate){
        _uiState.update { currentState -> currentState.copy(currentSelectedDate = date) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BetterOrioksApplication)
                //val academicPerformanceRepository =
                BetterOrioksViewModel(
                    //academicPerformanceRepository = academicPerformanceRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }

    fun getToken(loginDetails: String){
        println("GET_TOKEN")
        val encodedLoginDetails = Base64.getEncoder().encodeToString(loginDetails.toByteArray())
        val tokenRepository = NetworkTokenRepository(encodedLoginDetails)
        viewModelScope.launch {
            val token: Token = try{
                tokenRepository.getToken()
            }
            catch
                (e:HttpException)
            {
                val error = when(e.code()){
                    401 -> AuthState.TokenLimitReached
                    403 -> AuthState.BadLoginOrPassword
                    else -> AuthState.UnexpectedError
                }
                _uiState.update { currentState -> currentState.copy(authState = error) }
                Token()
            }
            if (token.token != "") {
                userPreferencesRepository.setToken(token = token.token)
                _uiState.update { currentState -> currentState.copy(
                    token = token.token,
                    userInfoUiState = UserInfoUiState.NotStarted,
                    academicDebtsUiState = DebtsUiState.NotStarted,
                    subjectsUiState = SubjectsUiState.Loading,
                ) }
            }
        }
    }

    fun getUserInfo(){
        println("GET_USER_INFO")
        val mainRepository = NetworkMainRepository(uiState.value.token)
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(userInfoUiState = UserInfoUiState.Loading) }
            try {
                val userInfo = mainRepository.getUserInfo()
                if(userInfo.full_name != "") {
                    _uiState.update { currentState -> currentState.copy(userInfoUiState = UserInfoUiState.Success(userInfo)) }
                }else{
                    _uiState.update { currentState -> currentState.copy(userInfoUiState = UserInfoUiState.Error) }
                }
            }catch(e: HttpException){
                _uiState.update { currentState -> currentState.copy(userInfoUiState = UserInfoUiState.Error) }
            }catch(e: java.lang.Exception){
                _uiState.update { currentState -> currentState.copy(userInfoUiState = UserInfoUiState.Error) }
            }
        }
    }

    fun getImportantDates(){
        println("GET_IMPORTANT_DATES")
        val mainRepository = NetworkMainRepository(uiState.value.token)
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(importantDatesUiState = ImportantDatesUiState.Loading) }
            try {
                val importantDates = mainRepository.getImportantDates()
                _uiState.update { currentState -> currentState.copy(importantDatesUiState = ImportantDatesUiState.Success(importantDates)) }
            }catch(e: HttpException){
                Log.d("GET_IMPORTANT_DATES", e.toString())
                _uiState.update { currentState -> currentState.copy(importantDatesUiState = ImportantDatesUiState.Error) }
            }catch(e: java.lang.Exception){
                _uiState.update { currentState -> currentState.copy(importantDatesUiState = ImportantDatesUiState.Error) }
            }
        }
    }

    fun getAcademicDebts(){
        println("GET_ACADEMIC_DEBTS")
        val mainRepository = NetworkMainRepository(uiState.value.token)
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(academicDebtsUiState = DebtsUiState.Loading) }
            try {
                val academicDebts = mainRepository.getAcademicDebt()
                _uiState.update { currentState -> currentState.copy(academicDebtsUiState = DebtsUiState.Success(academicDebts))}
            }catch(e: HttpException){
                _uiState.update { currentState -> currentState.copy(academicDebtsUiState = DebtsUiState.Error) }
            }catch(e: java.lang.Exception){
                _uiState.update { currentState -> currentState.copy(academicDebtsUiState = DebtsUiState.Error) }
            }
        }
    }

    fun getTimeTableAndGroup(){
        println("GET_TIME_TABLE_AND_GROUP")
        val mainRepository = NetworkMainRepository(uiState.value.token)
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(timeTableUiState = TimeTableUiState.Loading) }
            try {
                if(uiState.value.importantDatesUiState == ImportantDatesUiState.NotStarted || uiState.value.importantDatesUiState == ImportantDatesUiState.Error) getImportantDates()
                if(uiState.value.userInfoUiState == UserInfoUiState.NotStarted || uiState.value.userInfoUiState == UserInfoUiState.Loading) getUserInfo()
                val timeTable = mainRepository.getTimeTable()
                val groups = mainRepository.getGroup()
                groups.forEach{ element ->
                    if((uiState.value.userInfoUiState as UserInfoUiState.Success).userInfo.group in element.name)
                        _uiState.update { currentState -> currentState.copy(groupId = element.id) }
                }
                getSchedule()
                _uiState.update { currentState -> currentState.copy(timeTableUiState = TimeTableUiState.Success(timeTable))}
            }catch(e: HttpException){
                Log.d("GET_TIME_TABLE", e.toString())
                _uiState.update { currentState -> currentState.copy(timeTableUiState = TimeTableUiState.Error) }
            }catch(e: java.lang.Exception){
                Log.d("GET_TIME_TABLE", e.toString())
                _uiState.update { currentState -> currentState.copy(timeTableUiState = TimeTableUiState.Error) }
            }
        }
    }

    private fun getSchedule(){
        println("GET_SCHEDULE")
        val scheduleRepository = NetworkScheduleRepository(token = uiState.value.token, id = uiState.value.groupId)
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(scheduleUiState = ScheduleUiState.Loading) }
            try {
                val schedule = scheduleRepository.getSchedule()
                _uiState.update { currentState -> currentState.copy(scheduleUiState = ScheduleUiState.Success(schedule)) }
            }catch(e: HttpException){
                Log.d("GET_SCHEDULE", e.toString())
                _uiState.update { currentState -> currentState.copy(scheduleUiState = ScheduleUiState.Error) }
            }catch(e: java.lang.Exception){
                Log.d("GET_SCHEDULE", e.toString())
                _uiState.update { currentState -> currentState.copy(scheduleUiState = ScheduleUiState.Error) }
            }
        }
    }

    private fun calculateWeekType(date: LocalDate):Int{
        val period = DAYS.between(date,LocalDate.parse((uiState.value.importantDatesUiState as ImportantDatesUiState.Success).dates.semester_start))
        val currentWeek = (period/7).toInt()
        return kotlin.math.abs(currentWeek % 4)
    }
    fun getScheduleList(context:Context): List<Schedule> {
        if(uiState.value.importantDatesUiState is ImportantDatesUiState.Success) {
            val date = uiState.value.currentSelectedDate
            val weekType = calculateWeekType(date)
            val list = if (uiState.value.scheduleUiState is ScheduleUiState.Success)
                (uiState.value.scheduleUiState as ScheduleUiState.Success).schedule
            else listOf()
            //ВНИМАНИЕ: КОСТЫЛЬ!!!
            val filteredList =
                list.filter { ((it.week == weekType || it.name.contains("20")) && it.day == date.dayOfWeek.value - 1) }
            val sortedList = filteredList.sortedBy { it -> it.clas }

            val finalList = mutableListOf<Schedule>()
            sortedList.forEach { it ->
                if (it.name.contains("(")) {
                    val start = it.name.indexOf("(")
                    val end = it.name.indexOf(")")
                    val slice = it.name.slice(start..end)
                    val count = slice.filter { char -> char.isDigit() }.toInt()
                    for (i in 1..if (count < 6) count else 5) {
                        finalList.add(
                            Schedule(
                                name = it.name.slice(0 until start),
                                type = it.type.ifBlank { ("Пара") },
                                day = it.day,
                                clas = it.clas + i - 1,
                                week = it.week,
                                location = it.location,
                                teacher = it.teacher
                            )
                        )
                    }
                } else {
                    finalList.add(it)
                }
            }
            for (i in 0 until finalList.size - 1) {
                if (finalList[i + 1].clas - finalList[i].clas > 1) {
                    finalList.add(
                        Schedule(
                            name = context.getString(R.string.window),
                            type = (1.5 * (finalList[i + 1].clas - finalList[i].clas - 1)).toString(),
                            clas = finalList[i].clas + 1
                        )
                    )
                }
            }
            return finalList.sortedBy { it.clas }
        }else
            return listOf()
    }
}