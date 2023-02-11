package com.studentapp.betterorioks.ui.screens
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.components.ErrorScreen
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.studentapp.betterorioks.ui.components.RoundedMark
import com.studentapp.betterorioks.ui.states.DebtsUiState


@Composable
fun AcademicDebtTopBar(onClick: () -> Unit = {}){
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .defaultMinSize(72.dp)
        ) {
            IconButton(onClick = onClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(R.string.back_button),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Row(modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize(), verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(R.drawable.debt),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = stringResource(R.string.Debts),
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}


@Composable
fun AcademicDebtElement(
    modifier: Modifier = Modifier,
    subjectName: String = stringResource(R.string.blank),
    userPoints: Double = 0.0,
    systemPoints: Int = 10,
    deadline: String = "",
    consultationSchedule: String = "",
    controlForm: String = "",
    teachers: List<String> = listOf()
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(end = 16.dp)) {
            Text(
                text = subjectName, modifier.padding(bottom = 16.dp, end = 16.dp)
            )
            if(deadline.isNotBlank()) {
                Divider(color = MaterialTheme.colors.primary)
                Text(
                    text = stringResource(id = R.string.before, deadline),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                )
            }
            if(consultationSchedule.isNotBlank()){
                Divider(color = MaterialTheme.colors.primary)
                Text(
                    text = stringResource(id = R.string.consultations,consultationSchedule),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                )
            }
            val controlFormString = if(controlForm == ""){ stringResource(R.string.not_specified) } else {controlForm}
            Divider(color = MaterialTheme.colors.primary)
            Text(text = stringResource(id = R.string.control_form,controlFormString),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
            )
            if(teachers.isNotEmpty()) {
                val teacherStringId = if (teachers.size == 1) {
                    R.string.teacher
                } else {
                    R.string.teachers
                }
                Divider(color = MaterialTheme.colors.primary)
                Text(
                    text = stringResource(
                        id = teacherStringId,
                        teachers.joinToString(separator = ", ")
                    ),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 8.dp, end = 8.dp)
                )
            }

        }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AcademicDebtScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    uiState: AppUiState = AppUiState(),
    viewModel: BetterOrioksViewModel
){
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 10.dp,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Scaffold(
            topBar = { AcademicDebtTopBar(onClick = onClick) },
            backgroundColor = MaterialTheme.colors.surface
        ) { innerPadding ->
            val pullRefreshState = rememberPullRefreshState((uiState.academicDebtsUiState == DebtsUiState.Loading), { viewModel.getAcademicDebts() })
            Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxSize()
                ) {
                    when (uiState.academicDebtsUiState) {
                        DebtsUiState.Error -> {
                            item { ErrorScreen(modifier = Modifier.fillMaxSize()) }
                        }
                        is DebtsUiState.Success -> {
                            val debts = (uiState.academicDebtsUiState as DebtsUiState.Success).debts
                            if (debts.isNotEmpty()) {
                                items(debts) {
                                    AcademicDebtElement(
                                        subjectName = it.name,
                                        userPoints = it.current_grade,
                                        systemPoints = it.max_grade.toInt(),
                                        deadline = it.deadline,
                                        consultationSchedule = it.consultation_schedule,
                                        controlForm = it.control_form,
                                        teachers = it.teachers
                                    )
                                }
                            } else {
                                item {
                                    Image(
                                        painterResource(id = R.drawable.done_img),
                                        contentDescription = null,
                                        modifier = Modifier.wrapContentSize(Alignment.Center)
                                    )
                                }
                                item {
                                    Text(
                                        text = stringResource(R.string.no_debts),
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxSize(),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        else -> {
                            item { LoadingScreen(modifier = Modifier.fillMaxSize()) }
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = uiState.isAcademicPerformanceRefreshing,
                    state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter),
                    contentColor = MaterialTheme.colors.secondary
                )
            }
            }
        }
    }