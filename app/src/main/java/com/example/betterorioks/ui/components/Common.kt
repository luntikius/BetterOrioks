package com.example.betterorioks.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.betterorioks.R
import com.example.betterorioks.model.BottomNavItem

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
        modifier = modifier.fillMaxSize()
    ) {
        LoadingAnimation(circleColor = MaterialTheme.colors.secondary)
    }
}
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.loading_failed))
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Schedule,
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

//@Composable
//fun TopBar(onClick: () -> Unit = {}, screenName: String, showRefreshButton:Boolean){
//    Surface(modifier = Modifier.wrapContentSize(), color = MaterialTheme.colors.primaryVariant) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .wrapContentSize()
//                .padding(horizontal = 16.dp, vertical = 8.dp)
//        ) {
//            Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null, modifier = Modifier.size(30.dp).padding(16.dp))
//            Text(text = screenName, fontSize = 20.sp, color = MaterialTheme.colors.secondary)
//            Spacer(modifier = Modifier.weight(1f))
//            if(showRefreshButton) {
//                IconButton(onClick = onClick) {
//                    Icon(
//                        painter = painterResource(R.drawable.refresh),
//                        contentDescription = stringResource(R.string.back_button),
//                        tint = MaterialTheme.colors.secondary,
//                        modifier = Modifier.size(30.dp)
//                    )
//                }
//            }
//        }
//    }
//}