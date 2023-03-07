package com.studentapp.betterorioks.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.subjectsFromSite.ControlEvent
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.components.ErrorScreen
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.studentapp.betterorioks.ui.components.RoundedMark
import com.studentapp.betterorioks.ui.states.ImportantDatesUiState
import com.studentapp.betterorioks.ui.states.SubjectsFromSiteUiState
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@Preview
@Composable
fun ElementPreview(){
    AcademicPerformanceMoreElement()
}

private fun calculateWeeks(week:Int, uiState: AppUiState):Int{
    if (uiState.importantDatesUiState is ImportantDatesUiState.Success) {
        val dates = (uiState.importantDatesUiState as ImportantDatesUiState.Success).dates
        val start = dates.semesterStart
        val today = LocalDate.now()
        val startDate = LocalDate.parse(start)
        val weeks = week - (ChronoUnit.DAYS.between(startDate,today)/7).toInt() - 1
        if (weeks < 0) return -1
        return weeks
    }else
        return -2
}

@Composable
fun AcademicPerformanceMoreElement(
    modifier: Modifier = Modifier,
    subjectName: String = stringResource(R.string.blank),
    subjectShort:String = "",
    userPoints: Double = 0.0,
    systemPoints: Int = 10,
    weeks: Int = 0
){
    val finalSubjectShort = if(subjectShort != "-" && subjectShort != " "){" ($subjectShort)"}else{""}
    val weeksLeft = when(weeks) {
        -2 -> stringResource(R.string.date_is_not_specified)
        -1 -> stringResource(R.string.event_passed)
        0 -> stringResource(R.string.event_this_week)
        else -> stringResource(id = R.string.weeks_left, weeks)
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = weeksLeft,
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .wrapContentSize()
                .padding(end = 2.dp)
                .width(56.dp)
        )
        Divider(Modifier.fillMaxHeight().width(2.dp), color = MaterialTheme.colors.primary)
        Column(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(start = 4.dp,end = 16.dp)) {
            Text(
                text = "$subjectName$finalSubjectShort",
                modifier = modifier.padding(start = 4.dp)
            )
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
@Composable
fun AboutElement(
    uiState: AppUiState
){
    val controlForm = if(uiState.currentSubject.formOfControl.name == ""){ stringResource(R.string.not_specified) } else {uiState.currentSubject.formOfControl.name}
    Text(text = stringResource(id = R.string.control_form,controlForm),
        modifier = Modifier.padding(top = 16.dp,start = 16.dp,end = 16.dp, bottom = 4.dp),
        color = MaterialTheme.colors.primary,
        fontSize = 16.sp
    )
    val teacherStringId = if(uiState.currentSubject.teachers.size == 1){
        R.string.teacher
    }else{R.string.teachers}
    Text(text = stringResource(id = teacherStringId,uiState.currentSubject.teachers.joinToString(separator = ", ")),
        modifier = Modifier.padding(start = 16.dp,end = 16.dp, bottom = 16.dp),
        color = MaterialTheme.colors.primary,
        fontSize = 16.sp
    )
}


@Composable
fun TopPerformanceMoreBar(onClick: () -> Unit = {}, uiState: AppUiState){
    val currentSubject = uiState.currentSubject
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp
    ) {
        Column() {
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
                    text = stringResource(id = R.string.of, currentSubject.getMaxScore().toString()),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 8.dp)
                )
                RoundedMark(
                    userPoints = if(currentSubject.grade.fullScore == "-") -2.0 else currentSubject.grade.fullScore.toDouble(),
                    systemPoints = currentSubject.getMaxScore()
                )
            }
        }
    }
}
@Composable
fun AcademicPerformanceMoreScreen(uiState: AppUiState, onButtonClick: () -> Unit){
    AcademicPerformanceMore(
        disciplines = (uiState.subjectsFromSiteUiState as SubjectsFromSiteUiState.Success).subjects.subjects.filter{ it.id == uiState.currentSubject.id }[0].getControlEvents(),
        uiState = uiState,
        onButtonClick = onButtonClick,
        isLoading = false,
        isError = false,
        controlForm = uiState.currentSubject.formOfControl.name
    )
}


@Composable
fun AcademicPerformanceMore (
    modifier: Modifier = Modifier,
    uiState: AppUiState,
    onButtonClick: () -> Unit = {},
    disciplines: List<ControlEvent> = listOf(),
    controlForm: String = "",
    isLoading: Boolean,
    isError: Boolean
){
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 5.dp,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Scaffold(
            topBar = { TopPerformanceMoreBar(uiState = uiState, onClick = onButtonClick) },
            backgroundColor = MaterialTheme.colors.surface
        ) { innerPadding ->
            if (!isLoading && !isError) {
                LazyColumn(
                    Modifier
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    items(disciplines) {
                        AcademicPerformanceMoreElement(
                            subjectName = it.type.name.ifBlank { controlForm },
                            userPoints = if (it.grade.score == "-") -2.0 else if (it.grade.score == "н") -1.0 else it.grade.score.toDouble(),
                            systemPoints = it.maxScore,
                            subjectShort = it.shortName,
                            weeks = calculateWeeks(it.week, uiState)
                        )
                    }
                    item { AboutElement(uiState = uiState) }
                }
            }else if(isLoading){
                LoadingScreen(modifier = Modifier.fillMaxSize())
            }else{
                ErrorScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}