package com.example.betterorioks.ui

import androidx.lifecycle.ViewModel
import com.example.betterorioks.data.AppUiState
import com.example.betterorioks.data.Subject
import com.example.betterorioks.data.temp.Subjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BetterOrioksViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(a = 2))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun getAcademicPerformance(): List<Subject>{
        return Subjects
    }
}