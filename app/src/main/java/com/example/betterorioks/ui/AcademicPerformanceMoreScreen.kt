package com.example.betterorioks.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterorioks.R
import com.example.betterorioks.data.AppUiState
import com.example.betterorioks.data.Subject
import com.example.betterorioks.ui.components.RoundedMark

@Composable
fun AcademicPerformanceMoreElement(
    modifier: Modifier = Modifier,
    subjectName: String = stringResource(R.string.blank),
    userPoints: Double = 0.0,
    systemPoints: Int = 10,
){
    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = subjectName,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end = 16.dp),
            )
            Text(
                text = stringResource(id = R.string.of, systemPoints),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 8.dp)
            )
            RoundedMark(userPoints = userPoints, systemPoints = systemPoints)
        }
        Divider(color = MaterialTheme.colors.onBackground)
    }
}

@Composable
fun TopPerformanceMoreBar(onClick: () -> Unit = {}, uiState: AppUiState){
    val currentSubject = uiState.currentSubject
    Card(backgroundColor = MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(R.string.back_button),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = currentSubject.name,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end = 16.dp),
            )
            Text(
                text = stringResource(id = R.string.of, currentSubject.max_grade),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 8.dp)
            )
            RoundedMark(
                userPoints = currentSubject.current_grade,
                systemPoints = currentSubject.max_grade
            )
        }
    }
}

@Composable
fun AcademicPerformanceMore (modifier: Modifier = Modifier, getAcademicPerformance: () -> List<Subject>, uiState: AppUiState, onButtonClick: () -> Unit = {}){
    Card(
        shape = RoundedCornerShape(32.dp),
        elevation = 10.dp,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Scaffold(topBar = {TopPerformanceMoreBar(uiState = uiState, onClick = onButtonClick)}
        ) { innerPadding ->
            LazyColumn(
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)) {
                items(getAcademicPerformance()) {
                    AcademicPerformanceMoreElement(
                        subjectName = it.name,
                        userPoints = it.current_grade,
                        systemPoints = it.max_grade,
                    )
                }
            }
        }
    }
}