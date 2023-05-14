package com.studentapp.betterorioks.ui.screens
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.subjectsFromSite.ControlEvent
import com.studentapp.betterorioks.model.subjectsFromSite.DebtControlEvent
import com.studentapp.betterorioks.model.subjectsFromSite.Resource
import com.studentapp.betterorioks.model.subjectsFromSite.Teacher
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
    weeks: Int = 0,
    resources: List<Resource> = listOf(),
    onClick: () -> Unit = {}
){
    val finalSubjectShort = if(subjectShort != "-" && subjectShort != " "){" ($subjectShort)"}else{""}
    val weeksLeft = when(weeks) {
        -2 -> stringResource(R.string.date_is_not_specified)
        -1 -> stringResource(R.string.event_passed)
        0 -> stringResource(R.string.event_this_week)
        else -> stringResource(id = R.string.weeks_left, weeks)
    }
    val mod = if(resources.isNotEmpty())
        modifier.clickable {
            onClick()
        }
    else
        modifier

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = mod
            .padding(horizontal = 8.dp,vertical = 16.dp)
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
        Divider(
            Modifier
                .fillMaxHeight()
                .width(2.dp), color = MaterialTheme.colors.primary)
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
        Spacer(modifier = Modifier.size(8.dp))
        if(resources.isNotEmpty())
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = stringResource(id = R.string.move_next),
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colors.primary
            )
        else{
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
    Divider(color = MaterialTheme.colors.onBackground)
}
@Composable
fun AboutElement(
    uiState: AppUiState
){
    val controlForm = if(uiState.currentSubject.formOfControl.name == ""){ stringResource(R.string.not_specified) } else {uiState.currentSubject.formOfControl.name}
    Text(text = stringResource(id = R.string.control_form,controlForm),
        modifier = Modifier.padding(top = 8.dp,start = 16.dp,end = 16.dp, bottom = 8.dp),
        color = MaterialTheme.colors.primary,
        fontSize = 16.sp
    )
}

@Composable
fun TeacherElement(teacher: Teacher){
    Text(text = teacher.name,
        modifier = Modifier.padding(start = 16.dp,end = 16.dp, bottom = 2.dp),
        color = MaterialTheme.colors.primary,
        fontSize = 16.sp
    )
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("mailto:${teacher.email}")) }
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = MaterialTheme.colors.primary, fontSize = 16.sp)
        ){
            append(stringResource(id = R.string.email))
            append(" ")
        }
        withStyle(
            style = SpanStyle(color = MaterialTheme.colors.secondary, fontSize = 16.sp)
        ){
            append(teacher.email)
        }
    }
    ClickableText(text = annotatedString,
        onClick = { context.startActivity(intent) },
        modifier = Modifier.padding(start = 16.dp,end = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun TopPerformanceMoreBar(onClick: () -> Unit = {}, uiState: AppUiState){
    val currentSubject = uiState.currentSubject
    Card(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        shape = RoundedCornerShape(0),
    ) {
        Column {
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
            Divider()
        }
    }
}
@Composable
fun AcademicPerformanceMoreScreen(uiState: AppUiState, onButtonClick: () -> Unit, onControlEventClick: (ControlEvent) -> Unit, onResourceClick: () -> Unit){
    when(uiState.subjectsFromSiteUiState) {
        is SubjectsFromSiteUiState.Success -> {
            AcademicPerformanceMore(
                disciplines = (uiState.subjectsFromSiteUiState as SubjectsFromSiteUiState.Success).subjects.subjects.firstOrNull { it.id == uiState.currentSubject.id }
                    ?.getControlEvents() ?: listOf(),
                uiState = uiState,
                onButtonClick = onButtonClick,
                controlForm = uiState.currentSubject.formOfControl.name,
                setCurrentControlEvent = onControlEventClick,
                scienceId = uiState.currentSubject.scienceId,
                onResourceClick = onResourceClick,
                debtControlEvents = uiState.currentSubject.debtControlEvents,
                subjectsFromSiteUiState = uiState.subjectsFromSiteUiState
            )
        }
        is SubjectsFromSiteUiState.Error ->{
            onButtonClick()
        }
        else -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
    }
}
@Composable
fun ResourcesPopup(controlEvent: ControlEvent, onDismiss: () -> Unit, controlForm: String){
    val context = LocalContext.current
    Popup (
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true
        )
    ){
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
            elevation = 4.dp,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(0.7f),
            backgroundColor = MaterialTheme.colors.background,
        ) {
            Column() {
                Surface(
                    color = MaterialTheme.colors.primaryVariant
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                            .defaultMinSize(minHeight = 42.dp)
                    ) {
                        Text(
                            text = controlEvent.name.ifBlank { controlEvent.type.name.ifBlank { controlForm } },
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .weight(1f)
                                .fillMaxWidth()
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = stringResource(id = R.string.back_button),
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .clickable { onDismiss() }
                                .size(24.dp)
                        )
                    }
                }
                Divider()
                LazyColumn(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    item{ Spacer(modifier = Modifier.size(8.dp))}
                    items(controlEvent.resources){
                        val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(it.link)) }
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        context.startActivity(intent)
                                    } catch (_: Throwable) {
                                        Toast
                                            .makeText(
                                                context,
                                                context.getString(R.string.toast_unsupported_file_format),
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                        ) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = it.type,
                                        color = MaterialTheme.colors.primary,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Text(text = it.name)
                                }
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_forward),
                                    contentDescription = stringResource(id = R.string.move_next),
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    item{ Spacer(modifier = Modifier.size(8.dp))}
                }
            }
        }
    }
}

@Composable
fun DebtControlEventElement(debtControlEvent: DebtControlEvent){
    Column(
        Modifier
            .padding(top = 4.dp) ){
        Text(
            stringResource(R.string.resit),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical =  16.dp)
        )
        Divider(color = MaterialTheme.colors.onBackground)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(vertical = 8.dp)
        ) {
            Text("1", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Divider(
                Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .padding(vertical = 8.dp)
                ,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = stringResource(id = R.string.date_of_resit,debtControlEvent.date1), modifier = Modifier.padding(vertical = 2.dp))
                Text(text = stringResource(id = R.string.time_of_resit,debtControlEvent.time1), modifier = Modifier.padding(vertical = 2.dp))
                Text(text = stringResource(id = R.string.room_number, debtControlEvent.room1), modifier = Modifier.padding(vertical = 2.dp))
            }
        }
        Divider(color = MaterialTheme.colors.onBackground)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(vertical = 8.dp)
        ) {
            Text("2", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Divider(
                Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .padding(vertical = 8.dp)
                ,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = stringResource(id = R.string.date_of_resit,debtControlEvent.date2), modifier = Modifier.padding(vertical = 2.dp))
                Text(text = stringResource(id = R.string.time_of_resit,debtControlEvent.time2), modifier = Modifier.padding(vertical = 2.dp))
                Text(text = stringResource(id = R.string.room_number, debtControlEvent.room2), modifier = Modifier.padding(vertical = 2.dp))
            }
        }
        Divider(color = MaterialTheme.colors.onBackground)
    }
}

@Composable
fun BottomButtons(
    scienceId:Int,
    onResourceClick: () -> Unit
){
    val context = LocalContext.current
    val url = "https://orioks.miet.ru/mdl-gateway/course?science_id=${scienceId}"
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(url)) }
    Row(modifier = Modifier.padding(top = 16.dp)) {
        OutlinedButton(
            border = BorderStroke(1.dp,MaterialTheme.colors.primary),
            onClick = onResourceClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.secondary,
                disabledContentColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.Resources))
        }
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedButton(
            border = BorderStroke(1.dp,MaterialTheme.colors.primary),
            onClick = { context.startActivity(intent) },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.secondary
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.moodle_course))
        }
    }
}

@Composable
fun AcademicPerformanceMore (
    modifier: Modifier = Modifier,
    uiState: AppUiState,
    onButtonClick: () -> Unit = {},
    disciplines: List<ControlEvent> = listOf(),
    controlForm: String = "",
    scienceId: Int,
    setCurrentControlEvent: (ControlEvent) -> Unit = {},
    onResourceClick: () -> Unit,
    debtControlEvents: List<DebtControlEvent> = listOf(),
    subjectsFromSiteUiState: SubjectsFromSiteUiState
) {

    Scaffold(
        topBar = { TopPerformanceMoreBar(uiState = uiState,onClick = onButtonClick) },
        backgroundColor = MaterialTheme.colors.surface,
        modifier = modifier
    ) { innerPadding ->
        when (subjectsFromSiteUiState) {
            is SubjectsFromSiteUiState.Success -> {
                var popupIsVisible by remember { mutableStateOf(false) }
                if (popupIsVisible) {
                    ResourcesPopup(
                        uiState.currentControlEvent,
                        onDismiss = { popupIsVisible = false },
                        controlForm = controlForm
                    )
                }
                val mod = if (popupIsVisible) Modifier.blur(4.dp) else Modifier
                LazyColumn(
                    mod
                        .padding(innerPadding)
                        .padding(start = 16.dp,end = 16.dp,bottom = 8.dp)
                ) {
                    if (disciplines.isNotEmpty())
                        item {
                            Text(
                                stringResource(R.string.control_events),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            Divider(color = MaterialTheme.colors.onBackground)
                        }
                    items(disciplines) {
                        AcademicPerformanceMoreElement(
                            subjectName = it.type.name.ifBlank { controlForm },
                            userPoints = if (it.grade.score == "-") -2.0 else if (it.grade.score == "н") -1.0 else it.grade.score.toDouble(),
                            systemPoints = it.maxScore,
                            subjectShort = it.shortName,
                            weeks = calculateWeeks(it.week,uiState),
                            resources = it.resources,
                            onClick = {
                                setCurrentControlEvent(it)
                                popupIsVisible = true
                            }
                        )
                    }
                    items(debtControlEvents) {
                        DebtControlEventElement(debtControlEvent = it)
                    }
                    item { BottomButtons(scienceId = scienceId,onResourceClick = onResourceClick) }
                    item { AboutElement(uiState = uiState) }
                    item {
                        val teacherStringId = if (uiState.currentSubject.teachers.size == 1) {
                            R.string.teacher
                        }
                        else {
                            R.string.teachers
                        }
                        Text(
                            stringResource(id = teacherStringId),
                            modifier = Modifier.padding(start = 16.dp,end = 16.dp,bottom = 8.dp),
                            color = MaterialTheme.colors.primary,
                            fontSize = 16.sp
                        )
                    }
                    items(uiState.currentSubject.teachers) {
                        TeacherElement(teacher = it)
                    }
                }
            }
            is SubjectsFromSiteUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            else -> ErrorScreen(modifier = Modifier.fillMaxSize())
        }
    }
}