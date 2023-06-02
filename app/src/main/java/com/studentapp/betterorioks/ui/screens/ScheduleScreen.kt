package com.studentapp.betterorioks.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.rememberSplineBasedDecay
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.data.AppDetails.changeable
import com.studentapp.betterorioks.model.scheduleFromSite.SimpleScheduleElement
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.components.ErrorScreen
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.studentapp.betterorioks.ui.states.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
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

fun dayOfWeekToInt(day: LocalDate):Int{
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

fun dateToWeekType(day:LocalDate,context: Context,semesterStart:LocalDate): String{
    return when((DAYS.between(semesterStart,day)%28).toInt()/7){
        0 -> context.getString(R.string.type1)
        1 -> context.getString(R.string.type2)
        2 -> context.getString(R.string.type3)
        else -> context.getString(R.string.type4)

    }
}

fun minutesToText(minutes: Int):String{
    return if (minutes < 60) "Окно $minutes минут"
    else{
        val hours:Int = minutes/60
        val finalMinutes = minutes - hours * 60
        val hoursSpelling = when(hours){
            1 -> "час"
            in 2..4 -> "часа"
            else -> "часов"
        }
        "Окно $hours $hoursSpelling $finalMinutes минут"
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
            .padding(start = 4.dp, end = 4.dp, bottom = 16.dp, top = 8.dp)
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
    coroutineScope: CoroutineScope,
    lazyRowState: LazyListState,
    setDate: (date:LocalDate) -> Unit
){
    val startDate = LocalDate.now().minusDays(BACK_ITEMS.toLong())
    var isInitialComposition by remember{ mutableStateOf(false) }
    val initialIndex = DAYS.between(startDate,uiState.currentSelectedDate)
    val visibleIndex = lazyRowState.firstVisibleItemIndex

    Column(modifier = modifier.background(color = MaterialTheme.colors.background)) {
        Card(
            shape = RoundedCornerShape(0),
            backgroundColor = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.clickable {
                coroutineScope.launch {
                    lazyListState.scrollToItem(
                        abs(
                            DAYS.between(
                                startDate,
                                LocalDate.now()
                            )
                        ).toInt()
                    )
                }
                setDate(LocalDate.now())
            }
        ) {
            Text(
                text = monthToString(startDate.plusDays(visibleIndex.toLong()), context = LocalContext.current),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                if (uiState.importantDatesUiState is ImportantDatesUiState.Success) dateToWeekType(
                    uiState.currentSelectedDate,
                    LocalContext.current,
                    LocalDate.parse((uiState.importantDatesUiState as ImportantDatesUiState.Success).dates.semesterStart)
                )
                else "...",
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
            Text("", modifier = Modifier.padding(horizontal = 8.dp))
            Text(
                if (uiState.importantDatesUiState is ImportantDatesUiState.Success)
                    "${abs(DAYS.between(uiState.currentSelectedDate,LocalDate.parse((uiState.importantDatesUiState as ImportantDatesUiState.Success).dates.semesterStart)))/7+1} - неделя"
                else "...",
                fontSize = 14.sp,

                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
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
                        setDate(date)
                        coroutineScope.launch {
                            lazyListState.scrollToItem(
                                abs(
                                    DAYS.between(
                                        startDate,
                                        date
                                    )
                                ).toInt()
                            )
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun RefreshAlert(isAlert: Boolean, onDismiss: () -> Unit, onRefresh: () -> Unit){
    if(isAlert)
        AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Column {
                    Text(
                        stringResource(R.string.attention),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colors.onSurface
                    )
                    Text(
                        stringResource(id = R.string.refresh_alert_text),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                } },
            buttons = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    TextButton(
                        onClick = {
                            onRefresh()
                            onDismiss()
                                  },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(end = 8.dp,bottom = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.Refresh),
                            color = MaterialTheme.colors.error,
                        )
                    }
                    TextButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(end = 16.dp,bottom = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colors.secondary,
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()

        )
}


@Composable
fun ScheduleItem(it: SimpleScheduleElement, recalculateWindows: (Int,Int) -> Unit){
    if (!it.isWindow)
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp,vertical = 3.dp)
                .defaultMinSize(minHeight = 72.dp),
            elevation = if (MaterialTheme.colors.surface == Color.White) 4.dp else 0.dp
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp,vertical = 8.dp)
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
                                text = it.number.toString(),
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
                        val timeFrom = LocalDateTime.parse(it.from).toLocalTime()
                        val timeTo = LocalDateTime.parse(it.to).toLocalTime()
                        Text(text = "$timeFrom - $timeTo")
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
                        text = stringResource(id = R.string.room_number,it.room),
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                if(it.number in changeable)
                    IconButton(onClick = { recalculateWindows(it.day,it.number) }, modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)) {
                        Icon(
                            painterResource(id = R.drawable.swap_vert),
                            contentDescription = stringResource(R.string.change_lesson_time),
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colors.primary
                        )
                    }
            }
        } else
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp,vertical = 3.dp)
                    .defaultMinSize(minHeight = 72.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
                    minutesToText(it.windowDuration.toInt()),
                )
            }
}

@Composable
fun ScheduleList(viewModel: BetterOrioksViewModel, modifier: Modifier = Modifier, date: LocalDate ){
    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (viewModel.getTodaysSchedule(date).isNotEmpty()) {
            items(viewModel.getTodaysSchedule(date)) {
                ScheduleItem(
                    it = it,
                    recalculateWindows = {day, number -> viewModel.recalculateWindows(day = day, number = number)}
                )
            }
            item{ Spacer(modifier = Modifier.size(8.dp))}
        } else {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.size(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.happy_flame),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalSnapperApi::class)
@Composable
fun Schedule(
    viewModel: BetterOrioksViewModel,
    modifier: Modifier = Modifier,
    uiState: AppUiState
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isAlert by remember {
        mutableStateOf(false)
    }

    val pullRefreshState = rememberPullRefreshState(
        (uiState.fullScheduleUiState == FullScheduleUiState.Loading),
        { isAlert = true }
    )

    val startDate = LocalDate.now().minusDays(BACK_ITEMS.toLong())
    val lazyRowState = rememberLazyListState()

    RefreshAlert(isAlert = isAlert, onDismiss = { isAlert = false }, onRefresh = { viewModel.getFullSchedule(refresh = true, context = context) })

    BoxWithConstraints(
        modifier = modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val lazyListState: LazyListState = rememberLazyListState()
        Column {
            DatePicker(
                uiState = uiState,
                lazyListState = lazyListState,
                coroutineScope = coroutineScope,
                lazyRowState = lazyRowState,
                setDate = {date -> viewModel.setCurrentDateWithMovingTopBar(date = date, lazyRowState = lazyRowState, coroutineScope = coroutineScope, startDate = startDate )}
            )
            if (uiState.fullScheduleUiState is FullScheduleUiState.Success
                && uiState.importantDatesUiState is ImportantDatesUiState.Success
                && uiState.userInfoUiState is UserInfoUiState.Success
            ) {
                LaunchedEffect(lazyListState.isScrollInProgress) {
                    if (!lazyListState.isScrollInProgress) {
                        viewModel.setCurrentDateWithMovingTopBar(
                            date = startDate.plusDays(lazyListState.firstVisibleItemIndex.toLong()),
                            lazyRowState = lazyRowState,
                            coroutineScope = coroutineScope,
                            startDate = startDate
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if(!uiState.scheduleInitUiState) {
                        LoadingScreen(
                            modifier = Modifier
                                .wrapContentSize(Alignment.Center)
                                .fillMaxSize()
                        )
                    }
                    val mod = if(!uiState.scheduleInitUiState) Modifier.size(0.dp) else Modifier
                    LazyRow(
                        modifier = mod.fillMaxSize(),
                        state = lazyListState,
                        flingBehavior = rememberSnapperFlingBehavior(
                            lazyListState = lazyListState,
                            snapIndex = { _, startIndex, targetIndex ->
                                targetIndex.coerceIn(
                                    startIndex - 1,
                                    startIndex + 1
                                )
                            },
                            snapOffsetForItem = SnapOffsets.Start,
                            decayAnimationSpec = rememberSplineBasedDecay()
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(300) {
                            val date = startDate.plusDays(it.toLong())
                            ScheduleList(
                                viewModel = viewModel,
                                date = date,
                                modifier = Modifier.width(screenWidth.dp)
                            )
                        }
                    }
                }
            } else if (
                uiState.fullScheduleUiState is FullScheduleUiState.Error||
                uiState.importantDatesUiState is ImportantDatesUiState.Error||
                uiState.userInfoUiState is UserInfoUiState.Error
            ) {
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
            } else {
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
        }
        PullRefreshIndicator(
            refreshing = uiState.fullScheduleUiState is FullScheduleUiState.Loading,
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