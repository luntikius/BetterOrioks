package com.example.betterorioks.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterorioks.R
import com.example.betterorioks.ui.AppUiState
import com.example.betterorioks.ui.states.TimeTableUiState
import java.time.LocalDate

private fun dayOfWeekToString(day: LocalDate, context: Context):String{
    return when(day.dayOfWeek.toString()){
        "MONDAY" -> "Пн"
        "TUESDAY" -> "Вт"
        "WEDNESDAY" -> "Ср"
        "THURSDAY" -> "Чт"
        "FRIDAY" -> "Пт"
        "SATURDAY" -> "Сб"
        "SUNDAY" -> "Вс"
        else -> "err"
    }
}

@Composable
fun DatePickerElement(
    date: LocalDate = LocalDate.now(),
    isSelected: Boolean = false,
    onClick: (LocalDate) -> Unit = {},

){
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val elementWidth = screenWidth/7
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick(date) }
            .width(width = elementWidth.dp)
            .padding(vertical = 16.dp, horizontal = 4.dp)
    ) {
        Text(
            text = dayOfWeekToString(date, LocalContext.current)
        )

        Spacer(modifier = Modifier.size(4.dp))
        Card(
            shape = RoundedCornerShape(50),
            backgroundColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.background,
            modifier = Modifier.size((elementWidth/1.75).dp)
        ) {
            Text(
                date.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun ScheduleScreen(uiState:AppUiState){
    LazyRow(){
        items(270){it ->
            DatePickerElement(date = LocalDate.now().plusDays(it.toLong()), isSelected = true)
        }
    }
}