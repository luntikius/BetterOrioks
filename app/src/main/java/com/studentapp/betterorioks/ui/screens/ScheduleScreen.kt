package com.studentapp.betterorioks.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.schedule.Schedule
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.components.ErrorScreen
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.studentapp.betterorioks.ui.states.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS
import kotlin.math.abs

const val BACK_ITEMS = 28
private fun dayOfWeekToString(day: LocalDate, context: Context):String{
    return when(day.dayOfWeek.toString()){
        "MONDAY" -> context.getString(R.string.monday_short)
        "TUESDAY" -> context.getString(R.string.tuesday_short)
        "WEDNESDAY" -> context.getString(R.string.wednesday_short)
        "THURSDAY" -> context.getString(R.string.thursday_short)
        "FRIDAY" -> context.getString(R.string.friday_short)
        "SATURDAY" -> context.getString(R.string.saturday_short)
        "SUNDAY" -> context.getString(R.string.sunday_short)
        else -> context.getString(R.string.error_short)
    }
}

private fun monthToString(day: LocalDate, context: Context):String{
    return when(day.month.toString()){
        "JANUARY" -> context.getString(R.string.january)
        "FEBRUARY" -> context.getString(R.string.february)
        "MARCH" -> context.getString(R.string.march)
        "APRIL" -> context.getString(R.string.april)
        "MAY" -> context.getString(R.string.may)
        "JUNE" -> context.getString(R.string.june)
        "JULY" -> context.getString(R.string.july)
        "AUGUST" -> context.getString(R.string.august)
        "SEPTEMBER" -> context.getString(R.string.september)
        "OCTOBER" -> context.getString(R.string.october)
        "NOVEMBER" -> context.getString(R.string.november)
        "DECEMBER" -> context.getString(R.string.december)
        else -> context.getString(R.string.error_short)
    }
}

private fun dayOfWeekToInt(day: LocalDate):Int{
    return when(day.dayOfWeek.toString()){
        "MONDAY" -> 0
        "TUESDAY" -> 1
        "WEDNESDAY" -> 2
        "THURSDAY" -> 3
        "FRIDAY" -> 4
        "SATURDAY" -> 5
        "SUNDAY" -> 6
        else -> 0
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
        Card(elevation = 0.dp,
            shape = RoundedCornerShape(50),
            backgroundColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.background,
            modifier = Modifier.size((elementWidth/1.75).dp)
        ) {
            Text(
                date.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

//Don't know how to make it better :(
@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
fun DatePicker(
    uiState: AppUiState,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    coroutineScope: CoroutineScope
){
    val lazyRowState = rememberLazyListState()
    val startDate = LocalDate.now().minusDays(BACK_ITEMS.toLong())
    var isInitialComposition by remember{ mutableStateOf(false) }
    val initialIndex = DAYS.between(startDate,uiState.currentSelectedDate)
    val visibleIndex = lazyRowState.firstVisibleItemIndex

    Column(modifier = modifier.background(color = MaterialTheme.colors.background)) {
        Card(shape = RoundedCornerShape(0), backgroundColor = MaterialTheme.colors.primaryVariant) {
            Text(
                text = monthToString(startDate.plusDays(visibleIndex.toLong()), context = LocalContext.current),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        coroutineScope.launch {
                            lazyListState.scrollToItem(abs(DAYS.between(startDate,LocalDate.now())).toInt())
                        }
                        coroutineScope.launch {
                            lazyRowState.scrollToItem(BACK_ITEMS - dayOfWeekToInt(LocalDate.now()))
                        }
                    }
                ,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        LazyRow(state = lazyRowState) {
            if (!isInitialComposition) {
                coroutineScope.launch {
                    lazyRowState.scrollToItem(initialIndex.toInt() - dayOfWeekToInt(LocalDate.now()))
                    lazyListState.scrollToItem(abs(DAYS.between(startDate,LocalDate.now())).toInt())
                    isInitialComposition = true
                }
            }
            items(300) {
                val date = startDate.plusDays(it.toLong())
                DatePickerElement(
                    date = date,
                    isSelected = (uiState.currentSelectedDate == date),
                    onClick = {
                        coroutineScope.launch { lazyListState.scrollToItem(abs(DAYS.between(startDate,date)).toInt()) }
                    }
                )
            }
        }
    }
}

@Composable
fun ScheduleItem(it: Schedule, times: List<List<String>>){
    val time = times[it.clas - 1]
    if (it.name != "Окно")
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp, vertical = 3.dp)
                .defaultMinSize(minHeight = 72.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Text(
                            text = it.clas.toString(),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(4.dp)
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = it.type,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(4.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${time[0]} - ${time[1]}")
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = it.name,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = it.teacher,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = stringResource(id = R.string.room_number, it.location),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        } else
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp, vertical = 3.dp)
                .defaultMinSize(minHeight = 72.dp)
        )
        {
            Row(
                modifier = Modifier.wrapContentSize(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.window
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    "Окно ${it.type} часа",
                )
            }
        }
}

@Composable
fun ScheduleList(viewModel: BetterOrioksViewModel, uiState: AppUiState, modifier: Modifier = Modifier, date: LocalDate ){
    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (viewModel.getTodaysSchedule(date).isNotEmpty()) {
            items(viewModel.getTodaysSchedule(date)) {
                ScheduleItem(
                    it = it,
                    times = (uiState.timeTableUiState as TimeTableUiState.Success).timeTable.times
                )
            }
        } else {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.happy_flame),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.free_day),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 64.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalMaterialApi::class, ExperimentalSnapperApi::class)
@Composable
fun Schedule(
    viewModel: BetterOrioksViewModel,
    modifier: Modifier = Modifier,
    uiState: AppUiState
) {
    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        (uiState.timeTableUiState == TimeTableUiState.Loading),
        { viewModel.getTimeTableAndGroup() })
    val startDate = LocalDate.now().minusDays(BACK_ITEMS.toLong())


    BoxWithConstraints(
        modifier = modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val lazyListState: LazyListState = rememberLazyListState()
        Column() {
            DatePicker(
                uiState = uiState,
                lazyListState = lazyListState,
                coroutineScope = coroutineScope

            )
            if (uiState.timeTableUiState is TimeTableUiState.Success && uiState.scheduleUiState is ScheduleUiState.Success && uiState.importantDatesUiState is ImportantDatesUiState.Success) {
                viewModel.setCurrentDate(date = startDate.plusDays(lazyListState.firstVisibleItemIndex.toLong()))
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    flingBehavior = rememberSnapperFlingBehavior(lazyListState, snapIndex = { _, startIndex, targetIndex -> targetIndex.coerceIn(startIndex-1,startIndex+1)}),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(300) {
                        val date = startDate.plusDays(it.toLong())
                        ScheduleList(
                            viewModel = viewModel,
                            uiState = uiState,
                            date = date,
                            modifier = Modifier.width(screenWidth.dp)
                        )
                    }
                }
            } else if (
                uiState.timeTableUiState is TimeTableUiState.Loading || uiState.timeTableUiState == TimeTableUiState.NotStarted ||
                uiState.scheduleUiState is ScheduleUiState.Loading ||
                uiState.importantDatesUiState is ImportantDatesUiState.Loading||
                uiState.timeTableUiState is TimeTableUiState.Loading||
                uiState.userInfoUiState is UserInfoUiState.Loading
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        LoadingScreen(
                            modifier = Modifier
                                .wrapContentSize(Alignment.Center)
                                .fillMaxSize()
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        ErrorScreen(
                            modifier = Modifier
                                .wrapContentSize(Alignment.Center)
                                .fillMaxSize()
                        )
                    }
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



@Composable
fun ScheduleScreen(
    viewModel: BetterOrioksViewModel,
    uiState: AppUiState
){
    Column(modifier = Modifier.fillMaxSize()) {
        Schedule(
            viewModel = viewModel,
            uiState = uiState
        )
    }
}