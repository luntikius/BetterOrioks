package com.example.betterorioks.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ScheduleScreen(){
    Text(text ="Schedule Screen", fontSize = 40.sp, modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
}