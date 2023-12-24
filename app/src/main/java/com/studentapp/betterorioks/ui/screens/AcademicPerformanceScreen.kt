package com.studentapp.betterorioks.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.BetterOrioksScreens
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectFromSite
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectsData
import com.studentapp.betterorioks.ui.components.ErrorScreen
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.studentapp.betterorioks.ui.components.RoundedMark
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.components.ErrorDisplayText
import com.studentapp.betterorioks.ui.states.SubjectsFromSiteUiState
import com.studentapp.betterorioks.ui.states.UserInfoUiState
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
                    systemPoints = 12.0,
                )
                AcademicPerformanceElement(
                    subjectName = "Математика для квадратика",
                    userPoints = "50.0",
                    systemPoints = 100.0,
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
                    navController.navigate(BetterOrioksScreens.AcademicPerformanceMore.name){
                        launchSingleTop = true
                    }
                },
                viewModel = viewModel,
                uiState = uiState,
                changeSortedState = {viewModel.changeSortedState()}
            )
        is SubjectsFromSiteUiState.Error ->
            AcademicPerformance(
                viewModel = viewModel,
                uiState = uiState,
                isError = true,
                errorMessage = (uiState.subjectsFromSiteUiState as SubjectsFromSiteUiState.Error).err

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
    systemPoints: Double = 10.0,
    onClick: () -> Unit = {}
){

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = if (MaterialTheme.colors.surface == Color.White) 4.dp else 0.dp,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp,vertical = 3.dp)
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
            val parsedSystemPoints = if(systemPoints.toInt().toDouble() == systemPoints) systemPoints.toInt() else systemPoints
            Text(
                text = stringResource(id = R.string.of,parsedSystemPoints),
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

fun groupOfSubjects(
    scope:LazyListScope,subjects: List<SubjectFromSite>,
    onComponentClicked: () -> Unit,
    setCurrentSubject: (SubjectFromSite) -> Unit,
    @StringRes text:Int
){
    scope.item {
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(id = text),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
    scope.items(subjects) {
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
}
fun groupedSubjects(subjects:SubjectsData,scope:LazyListScope, onComponentClicked: () -> Unit, setCurrentSubject: (SubjectFromSite) -> Unit){
    val unfinishedSubjects = subjects.subjects.filter {it.grade.fullScore.toDoubleOrNull() == null || (it.grade.fullScore.toDoubleOrNull() !== null && it.grade.fullScore.toDouble() < 50)}
    val normalSubjects = subjects.subjects.filter { it.formOfControlId != 1 && it.grade.fullScore.toDoubleOrNull() != null && it.grade.fullScore.toDouble() >= 50 && it.grade.fullScore.toDouble() < 70 }
    val goodSubjects = subjects.subjects.filter { it.formOfControlId != 1 && it.grade.fullScore.toDoubleOrNull() != null && it.grade.fullScore.toDouble() >= 70 && it.grade.fullScore.toDouble() < 86 }
    val excellentSubjects = subjects.subjects.filter { it.formOfControlId != 1 && it.grade.fullScore.toDoubleOrNull() != null && it.grade.fullScore.toDouble() >= 86}
    val creditSubjects = subjects.subjects.filter { it.formOfControlId == 1 && it.grade.fullScore.toDoubleOrNull() != null && it.grade.fullScore.toDouble() >= 50}
    if(unfinishedSubjects.isNotEmpty()) {
        groupOfSubjects(
            scope = scope,
            subjects = unfinishedSubjects,
            onComponentClicked = onComponentClicked,
            setCurrentSubject = setCurrentSubject,
            text = R.string.bad_mark
        )
    }
    if(normalSubjects.isNotEmpty()) {
        groupOfSubjects(
            scope = scope,
            subjects = normalSubjects,
            onComponentClicked = onComponentClicked,
            setCurrentSubject = setCurrentSubject,
            text = R.string.ok_mark
        )
    }
    if(goodSubjects.isNotEmpty()) {
        groupOfSubjects(
            scope = scope,
            subjects = goodSubjects,
            onComponentClicked = onComponentClicked,
            setCurrentSubject = setCurrentSubject,
            text = R.string.good_mark
        )
    }
    if(excellentSubjects.isNotEmpty()) {
        groupOfSubjects(
            scope = scope,
            subjects = excellentSubjects,
            onComponentClicked = onComponentClicked,
            setCurrentSubject = setCurrentSubject,
            text = R.string.excellent_mark
        )
    }
    if(creditSubjects.isNotEmpty()){
        groupOfSubjects(
            scope = scope,
            subjects = creditSubjects,
            onComponentClicked = onComponentClicked,
            setCurrentSubject = setCurrentSubject,
            text = R.string.Credit
        )
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
    isError: Boolean = false,
    errorMessage: String = "",
    changeSortedState: () -> Unit = {}
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
                    ErrorScreen(modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth())

                }
                if(uiState.userInfoUiState is UserInfoUiState.Success &&
                    (uiState.userInfoUiState as UserInfoUiState.Success).userInfo.recordBookId.toString()
                    in AppDetails.debugUsers
                    ){
                    item{
                        ErrorDisplayText(errorMessage = errorMessage)
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()) {
                item {Spacer(modifier = Modifier.size(16.dp))}
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = modifier.size(16.dp))
                        Text(
                            text = stringResource(R.string.academic_performance_caps),
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { changeSortedState() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sort),
                                contentDescription = "Switch display mode",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = modifier.size(16.dp))
                    }
                }
                if(uiState.disciplineGrouping){
                    groupedSubjects(
                        subjects = subjects,
                        scope = this,
                        onComponentClicked = onComponentClicked,
                        setCurrentSubject = setCurrentSubject
                    )
                }else
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
                if(subjects.debts.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = stringResource(id = R.string.Debts),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                    items(subjects.debts) {
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
