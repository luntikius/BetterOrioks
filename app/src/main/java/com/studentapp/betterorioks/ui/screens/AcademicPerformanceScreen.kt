package com.example.betterorioks.ui

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
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.BetterOrioksScreens
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectFromSite
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectsData
import com.studentapp.betterorioks.ui.components.ErrorScreen
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.studentapp.betterorioks.ui.components.RoundedMark
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.states.SubjectsFromSiteUiState
import com.studentapp.betterorioks.ui.theme.BetterOrioksTheme

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MyPreview(){
    BetterOrioksTheme() {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            Column {
                AcademicPerformanceElement(
                    subjectName = "Изучение картофельных наук с добавлением перца чили и прочих",
                    userPoints = "10.0",
                    systemPoints = 12,
                )
                AcademicPerformanceElement(
                    subjectName = "Математика для квадратика",
                    userPoints = "50.0",
                    systemPoints = 100,
                )
                AcademicPerformanceElement()
            }
        }
    }
}

@Composable
fun AcademicPerformanceScreen(uiState: AppUiState, navController: NavHostController, viewModel: BetterOrioksViewModel){
    when(uiState.subjectsFromSiteUiState){
        is SubjectsFromSiteUiState.Success ->
            AcademicPerformance(
                subjects = (uiState.subjectsFromSiteUiState as SubjectsFromSiteUiState.Success).subjects,
                setCurrentSubject = {viewModel.setCurrentSubject(it)},
                onComponentClicked = {
                    navController.navigate(BetterOrioksScreens.AcademicPerformanceMore.name)
                },
                viewModel = viewModel,
                uiState = uiState
            )
        is SubjectsFromSiteUiState.Error ->
            AcademicPerformance(
                viewModel = viewModel,
                uiState = uiState,
                isError = true
            )
        else ->
            AcademicPerformance(
                viewModel = viewModel,
                uiState = uiState,
                isLoading = true
            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AcademicPerformanceElement(
    modifier: Modifier = Modifier,
    subjectName: String = stringResource(R.string.blank),
    userPoints: String = "-",
    systemPoints: Int = 10,
    onClick: () -> Unit = {}
){

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .defaultMinSize(minHeight = 72.dp)
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
            RoundedMark(userPoints = if(userPoints == "-") -2.0 else userPoints.toDouble(), systemPoints = systemPoints)
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
    subjects: SubjectsData = SubjectsData(),
    setCurrentSubject: (SubjectFromSite) -> Unit = {},
    viewModel: BetterOrioksViewModel,
    uiState: AppUiState,
    isLoading: Boolean = false,
    isError: Boolean = false
){
    val pullRefreshState =
        rememberPullRefreshState(uiState.subjectsFromSiteUiState == SubjectsFromSiteUiState.Loading, {
            viewModel.getAcademicPerformanceFromSite()
        })
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        if(isLoading) {
            LazyColumn(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                item{ LoadingScreen(modifier = Modifier.fillMaxSize()) }
            }
        } else if(isError){
            LazyColumn(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                item{
                    ErrorScreen(modifier = Modifier.wrapContentSize(Alignment.Center).fillMaxWidth())
                }
            }
        } else {
            LazyColumn(modifier = Modifier.pullRefresh(pullRefreshState).fillMaxSize()) {
                item {Spacer(modifier = Modifier.size(16.dp))}
                items(subjects.subjects) {
                    AcademicPerformanceElement(
                        subjectName = it.name,
                        userPoints = it.grade.fullScore,
                        systemPoints = it.getMaxScore(),
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
            refreshing = uiState.subjectsFromSiteUiState is SubjectsFromSiteUiState.Loading,
            state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.secondary
        )
    }

}
