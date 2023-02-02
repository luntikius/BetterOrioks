package com.example.betterorioks.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.betterorioks.ui.theme.BetterOrioksTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterorioks.R

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
fun RoundedMark(userPoints: Double, systemPoints: Int,modifier: Modifier = Modifier){
    val outlineColor = when((userPoints/systemPoints*100).toInt()){
        in 50..69 -> Color.Yellow
        in 70..85 -> colorResource(id = R.color.light_green)
        in 86..100 -> Color.Green
        else -> Color.Red
    }
    Card(
        shape = RoundedCornerShape(50),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = modifier.size(35.dp),
        border = BorderStroke(width = 2.dp,color = outlineColor)
    ){
        Text(
            text = userPoints.toInt().toString(),
            modifier = Modifier.wrapContentSize(),
            fontSize = 14.sp
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
            .clickable { onClick }
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


@Composable
fun AcademicPerformance(viewModel: BetterOrioksViewModel){
    LazyColumn(){
        items(viewModel.getAcademicPerformance()){
            AcademicPerformanceElement(
                subjectName = it.name,
                userPoints = it.current_grade,
                systemPoints = it.max_grade
            )
        }
    }
}
