package com.studentapp.betterorioks.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.studentapp.betterorioks.ui.states.TimeTableUiState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

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
    viewModel: BetterOrioksViewModel
){
    val lazyRowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val startDate = LocalDate.now().minusDays(BACK_ITEMS.toLong())
    var isInitialComposition by remember{ mutableStateOf(false) }
    val initialIndex = DAYS.between(startDate,uiState.currentSelectedDate)
    val visibleIndex = lazyRowState.firstVisibleItemIndex
    Column() {
        Card(shape = RoundedCornerShape(0), backgroundColor = MaterialTheme.colors.primaryVariant) {
            Text(
                text = monthToString(startDate.plusDays(visibleIndex.toLong()), context = LocalContext.current),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        coroutineScope.launch {
                            viewModel.setCurrentDate(LocalDate.now())
                            lazyRowState.animateScrollToItem(BACK_ITEMS - dayOfWeekToInt(LocalDate.now()))
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
                    isInitialComposition = true
                }
            }
            items(300) {
                val date = startDate.plusDays(it.toLong())
                DatePickerElement(
                    date = date,
                    isSelected = (uiState.currentSelectedDate == date),
                    onClick = {
                        viewModel.setCurrentDate(date)
                    }
                )
            }
        }
    }
}

@Composable
fun ScheduleItem(it: Schedule, times: List<List<String>>){
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
                    val time = times[it.clas - 1]
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Schedule(
    viewModel: BetterOrioksViewModel,
    modifier: Modifier = Modifier,
    uiState: AppUiState
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullRefreshState(
        (uiState.timeTableUiState == TimeTableUiState.Loading),
        { viewModel.getTimeTableAndGroup() })
    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        when (uiState.timeTableUiState) {
            is TimeTableUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { DatePicker(uiState = uiState, viewModel = viewModel) }
                    if (viewModel.getScheduleList(context = context).isNotEmpty()) {
                        items(viewModel.getScheduleList(context = context)) {
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
                                Text(text = stringResource(R.string.free_day))
                            }
                        }
                    }
                }
            }
            is TimeTableUiState.Loading -> {
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
            }
            else -> {
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