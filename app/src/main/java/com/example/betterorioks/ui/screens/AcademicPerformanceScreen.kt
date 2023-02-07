package com.example.betterorioks.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.betterorioks.R
import com.example.betterorioks.model.BetterOrioksScreens
import com.example.betterorioks.model.Subject
import com.example.betterorioks.ui.components.ErrorScreen
import com.example.betterorioks.ui.components.LoadingScreen
import com.example.betterorioks.ui.components.RoundedMark
import com.example.betterorioks.ui.states.SubjectsUiState
import com.example.betterorioks.ui.theme.BetterOrioksTheme

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MyPreview(){
    BetterOrioksTheme() {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            Column {
                AcademicPerformanceElement(
                    subjectName = "Изучение картофельных наук с добавлением перца чили и прочих",
                    userPoints = 10.0,
                    systemPoints = 12,
                )
                AcademicPerformanceElement(
                    subjectName = "Математика для квадратика",
                    userPoints = 50.0,
                    systemPoints = 100,
                )
                AcademicPerformanceElement()
            }
        }
    }
}

@Composable
fun AcademicPerformanceScreen(uiState: AppUiState, navController: NavHostController, viewModel: BetterOrioksViewModel){
    when(uiState.subjectsUiState){
        is SubjectsUiState.Success ->
            AcademicPerformance(
                subjects = (uiState.subjectsUiState as SubjectsUiState.Success).subjects,
                setCurrentSubject = {viewModel.setCurrentSubject(it)},
                onComponentClicked = {
                    navController.navigate(BetterOrioksScreens.AcademicPerformanceMore.name)
                },
                viewModel = viewModel,
                uiState = uiState
            )
        is SubjectsUiState.Loading ->
            AcademicPerformance(
                viewModel = viewModel,
                uiState = uiState,
                isLoading = true
            )
        is SubjectsUiState.Error ->
            AcademicPerformance(
                viewModel = viewModel,
                uiState = uiState,
                isError = true
            )
    }
}

@Composable
fun AcademicPerformanceElement(
    modifier: Modifier = Modifier,
    subjectName: String = stringResource(R.string.blank),
    userPoints: Double = 0.0,
    systemPoints: Int = 10,
    onClick: () -> Unit = {}
){

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 10.dp,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .defaultMinSize(minHeight = 72.dp)
            .clickable(onClick = onClick)
    ) {
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
                text = stringResource(id = R.string.of,systemPoints),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 8.dp)
            )
            RoundedMark(userPoints = userPoints, systemPoints = systemPoints)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = stringResource(R.string.move_next),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(20.dp)
            )
            }
        }
    }


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AcademicPerformance(
    modifier: Modifier = Modifier,
    onComponentClicked: () -> Unit = {},
    subjects:List<Subject> = listOf(),
    setCurrentSubject: (Subject) -> Unit = {},
    viewModel: BetterOrioksViewModel,
    uiState: AppUiState,
    isLoading: Boolean = false,
    isError: Boolean = false
){
    val pullRefreshState = rememberPullRefreshState(uiState.isAcademicPerformanceRefreshing, { viewModel.getAcademicPerformance() })
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        if(isLoading) {
            LoadingScreen()}
        else if(isError){
            ErrorScreen()
        }else {
            LazyColumn() {
                item {Spacer(modifier = Modifier.size(16.dp))}
                items(subjects) {
                    AcademicPerformanceElement(
                        subjectName = it.name,
                        userPoints = it.current_grade,
                        systemPoints = it.max_grade.toInt(),
                        onClick = {
                            onComponentClicked()
                            setCurrentSubject(it)
                        }
                    )
                }
                item {Spacer(modifier = Modifier.size(16.dp))}
            }
        }
        PullRefreshIndicator(
            refreshing = uiState.isAcademicPerformanceRefreshing,
            state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.secondary
        )
    }

}
