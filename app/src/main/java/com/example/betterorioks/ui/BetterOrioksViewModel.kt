package com.example.betterorioks.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.betterorioks.BetterOrioksApplication
import com.example.betterorioks.data.AcademicPerformanceRepository
import com.example.betterorioks.model.Subject
import com.example.betterorioks.ui.states.SubjectsUiState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BetterOrioksViewModel(
    private val academicPerformanceRepository: AcademicPerformanceRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun getAcademicPerformance(){
        viewModelScope.launch {
            _uiState.update { currentState -> currentState.copy(subjectsUiState = SubjectsUiState.Loading) }
            val subjectsUiState = try{
                val subjects = academicPerformanceRepository.getAcademicPerformance()
                SubjectsUiState.Success(subjects)
            }catch (e: java.lang.Exception){
                SubjectsUiState.Error
            }catch (e:HttpException){
                SubjectsUiState.Error
            }
            _uiState.update { currentState -> currentState.copy(subjectsUiState = subjectsUiState) }
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