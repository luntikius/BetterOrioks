package com.example.betterorioks.ui.screens
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
import com.example.betterorioks.R
import com.example.betterorioks.model.SubjectMore
import com.example.betterorioks.ui.AppUiState
import com.example.betterorioks.ui.components.ErrorScreen
import com.example.betterorioks.ui.components.LoadingScreen
import com.example.betterorioks.ui.components.RoundedMark
import com.example.betterorioks.ui.states.SubjectsMoreUiState


@Preview
@Composable
fun ElementPreview(){
    AcademicPerformanceMoreElement()
}
@Composable
fun AcademicPerformanceMoreElement(
    modifier: Modifier = Modifier,
    subjectName: String = stringResource(R.string.blank),
    subjectShort:String = "",
    subjectFullName:String = "",
    userPoints: Double = 0.0,
    systemPoints: Int = 10,
){
    val finalSubjectShort = if(subjectShort != ""){" ($subjectShort)"}else{""}
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(end = 16.dp)) {
            if (subjectFullName != "") {
                Text(
                    text = subjectFullName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 8.dp)
                )
            }
            Text(
                text = "$subjectName$finalSubjectShort"
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
    val controlForm = if(uiState.currentSubject.control_form == ""){ stringResource(R.string.not_specified) } else {uiState.currentSubject.control_form}
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
    Card(backgroundColor = MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp), elevation = 8.dp) {
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
                    text = stringResource(id = R.string.of, currentSubject.max_grade.toString()),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 8.dp)
                )
                RoundedMark(
                    userPoints = currentSubject.current_grade,
                    systemPoints = currentSubject.max_grade.toInt()
                )
            }
        }
    }
}
@Composable
fun AcademicPerformanceMoreScreen(uiState: AppUiState, onButtonClick: () -> Unit){
    when(uiState.currentSubjectDisciplines[uiState.currentSubject.id]){
        is SubjectsMoreUiState.Success -> AcademicPerformanceMore(
            disciplines = (uiState.currentSubjectDisciplines[uiState.currentSubject.id] as SubjectsMoreUiState.Success).Disciplines,
            uiState = uiState,
            onButtonClick = onButtonClick,
            isLoading = false,
            isError = false
        )
        is SubjectsMoreUiState.Loading -> AcademicPerformanceMore(
            isLoading = true,
            uiState = uiState,
            isError = false,
            onButtonClick = onButtonClick
        )
        else -> AcademicPerformanceMore(isLoading = true, isError = false, uiState = uiState, onButtonClick = onButtonClick)
    }
}


@Composable
fun AcademicPerformanceMore (
    modifier: Modifier = Modifier,
    uiState: AppUiState,
    onButtonClick: () -> Unit = {},
    disciplines: List<SubjectMore> = listOf(),
    isLoading: Boolean,
    isError: Boolean
){
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 10.dp,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Scaffold(topBar = { TopPerformanceMoreBar(uiState = uiState, onClick = onButtonClick) }
        ) { innerPadding ->
            if (!isLoading && !isError) {
                LazyColumn(
                    Modifier
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    items(disciplines) {
                        AcademicPerformanceMoreElement(
                            subjectName = it.type,
                            userPoints = it.current_grade,
                            systemPoints = it.max_grade.toInt(),
                            subjectFullName = it.name,
                            subjectShort = it.alias
                        )
                    }
                    item { AboutElement(uiState = uiState) }
                }
            }else if(isLoading){
                LoadingScreen()
            }else{
                ErrorScreen()
            }
        }
    }
}