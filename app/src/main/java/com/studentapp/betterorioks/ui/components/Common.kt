package com.studentapp.betterorioks.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.BottomNavItem

@Preview
@Composable
fun CommonPreview(){

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
        border = BorderStroke(
            width = 2.dp,
            color = if(userPoints.toInt() == -2 || systemPoints == 0) MaterialTheme.colors.secondary else outlineColor
        )
    ){
        Text(
            text =
            if (userPoints.toInt() >= 0) userPoints.toInt().toString()
            else if(userPoints.toInt() == -2) "-"
            else "Н",
            modifier = Modifier.wrapContentSize(),
            fontSize = 14.sp
        )
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(64.dp)
    ) {
        LoadingAnimation(circleColor = MaterialTheme.colors.secondary, modifier = Modifier.align(Alignment.Center))
    }
}
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.wrapContentSize(Alignment.Center)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.error_img),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                stringResource(R.string.loading_failed),
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.AcademicPerformance,
        BottomNavItem.Profile,
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = Color.Black,
        elevation = 8.dp,
        modifier = Modifier
            .wrapContentSize()
            .defaultMinSize(minHeight = 60.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val scheduleItem = BottomNavItem.Schedule
        BottomNavigationItem(
            icon = { Icon(painterResource(id = scheduleItem.icon), contentDescription = scheduleItem.title, modifier = Modifier.size(30.dp)) },
            label = {
                Text(
                    text = scheduleItem.title,
                    fontSize = 9.sp
                )
            },
            selectedContentColor = MaterialTheme.colors.secondary,
            unselectedContentColor = MaterialTheme.colors.secondary.copy(0.4f),
            alwaysShowLabel = true,
            selected = currentRoute == scheduleItem.screen_route,
            onClick = {
                navController.navigate(scheduleItem.screen_route) {

                    navController.graph.startDestinationRoute?.let { screen_route ->
                        popUpTo(screen_route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title, modifier = Modifier.size(30.dp)) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp
                    )
                },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = MaterialTheme.colors.secondary.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AnyButton (
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    @StringRes text: Int,
    icon: Int
){
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        onClick = onClick,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .defaultMinSize(minHeight = 72.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.size(16.dp))
            Icon(painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = stringResource(text),
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}