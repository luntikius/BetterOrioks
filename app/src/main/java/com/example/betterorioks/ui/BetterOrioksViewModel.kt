package com.example.betterorioks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.betterorioks.BetterOrioksApplication
import com.example.betterorioks.data.*
import com.example.betterorioks.model.Subject
import com.example.betterorioks.model.Token
import com.example.betterorioks.ui.states.AuthState
import com.example.betterorioks.ui.states.SubjectsMoreUiState
import com.example.betterorioks.ui.states.SubjectsUiState
import com.example.betterorioks.ui.states.UserInfoUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

    fun exit(){
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(authState = AuthState.Loading) }
            val exitRepository = NetworkExitRepository(uiState.value.token)
            try{
                exitRepository.removeToken()
                userPreferencesRepository.setToken("")
                _uiState.update { currentState -> currentState.copy(authState = AuthState.NotLoggedIn) }
            }catch(e:HttpException) {
                if (e.code() == 400){
                    userPreferencesRepository.setToken("")
                    _uiState.update { currentState -> currentState.copy(authState = AuthState.NotLoggedIn) }
                }
            }
        }
    }

    fun getAcademicPerformance(){
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
                (uiState.value.subjectsUiState as SubjectsUiState.Success).subjects.forEach{subject ->
                    getAcademicPerformanceMore(subject.id)
                }
            }
        }
    }

    private fun getAcademicPerformanceMore(disciplineId: Int){
        val academicPerformanceMoreRepository = NetworkAcademicPerformanceMoreRepository(disciplineId, token = uiState.value.token)
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(currentSubjectDisciplines = _uiState.value.currentSubjectDisciplines + Pair(disciplineId,SubjectsMoreUiState.Loading))}
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
        val encodedLoginDetails = Base64.getEncoder().encodeToString(loginDetails.toByteArray())
        val tokenRepository = NetworkTokenRepository(encodedLoginDetails)
        viewModelScope.launch {
            val token:Token = try{
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
                _uiState.update { currentState -> currentState.copy(token = token.token) }
            }
        }
    }

    fun getUserInfo(){
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
}