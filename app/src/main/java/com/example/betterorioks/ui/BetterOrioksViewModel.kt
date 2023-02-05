package com.example.betterorioks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.betterorioks.BetterOrioksApplication
import com.example.betterorioks.data.AcademicPerformanceRepository
import com.example.betterorioks.data.NetworkAcademicPerformanceMoreRepository
import com.example.betterorioks.model.Subject
import com.example.betterorioks.ui.states.SubjectsMoreUiState
import com.example.betterorioks.ui.states.SubjectsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
class BetterOrioksViewModel(
    private val academicPerformanceRepository: AcademicPerformanceRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()


    fun getAcademicPerformance(){
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(subjectsUiState = SubjectsUiState.Loading,isAcademicPerformanceRefreshing = true) }
            val subjectsUiState = try{
                val subjects = academicPerformanceRepository.getAcademicPerformance()
                SubjectsUiState.Success(subjects)
            }catch (e: java.lang.Exception){
                SubjectsUiState.Error
            }catch (e:HttpException){
                SubjectsUiState.Error
            }
            _uiState.update { currentState -> currentState.copy(subjectsUiState = subjectsUiState)}
            if(uiState.value.subjectsUiState is SubjectsUiState.Success) {
                (uiState.value.subjectsUiState as SubjectsUiState.Success).subjects.forEach{subject ->
                    getAcademicPerformanceMore(subject.id)
                }
            }
        }
    }

    private fun getAcademicPerformanceMore(disciplineId: Int){
        val academicPerformanceMoreRepository = NetworkAcademicPerformanceMoreRepository(disciplineId)
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
            _uiState.update { currentState -> currentState.copy(currentSubjectDisciplines = _uiState.value.currentSubjectDisciplines + Pair(disciplineId,subjectsUiState),isAcademicPerformanceRefreshing = false)}
        }

    }

    fun setCurrentSubject(subject: Subject){
        _uiState.update {currentState -> currentState.copy(currentSubject = subject)}
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BetterOrioksApplication)
                val academicPerformanceRepository = application.container.academicPerformanceRepository
                BetterOrioksViewModel(academicPerformanceRepository = academicPerformanceRepository)
            }
        }
    }
}