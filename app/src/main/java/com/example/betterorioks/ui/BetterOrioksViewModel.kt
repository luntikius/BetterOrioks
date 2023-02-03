package com.example.betterorioks.ui

import androidx.lifecycle.ViewModel
import com.example.betterorioks.data.AppUiState
import com.example.betterorioks.data.Subject
import com.example.betterorioks.data.temp.Subjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BetterOrioksViewModel: ViewModel() {
    val TAG = "ViewModel"
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun getAcademicPerformance(): List<Subject>{
        return Subjects
    }

    fun setCurrentSubject(subject:Subject){
        _uiState.update {currentState -> currentState.copy(currentSubject = subject)}
    }
}