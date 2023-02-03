package com.example.betterorioks.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterorioks.R

@Preview
@Composable
fun CommonPreview(){

}

@Composable
fun BottomBar () {

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