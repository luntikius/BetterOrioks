package com.example.betterorioks.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.betterorioks.ui.AppUiState
import com.example.betterorioks.ui.states.TimeTableUiState

@Composable
fun ScheduleScreen(uiState:AppUiState){
    if(uiState.timeTableUiState is TimeTableUiState.Success)
        Text(text = (uiState.timeTableUiState as TimeTableUiState.Success).timeTable.times.toString(),
            fontSize = 40.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
}