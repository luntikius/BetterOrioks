package com.studentapp.betterorioks.ui.components

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.studentapp.betterorioks.MainActivity
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.BottomNavItem


@Preview
@Composable
fun CommonPreview(){

}

@Composable
fun RoundedMark(userPoints: Double, systemPoints: Double,modifier: Modifier = Modifier, isBonus: Boolean = false){
    val outlineColor = when((userPoints/systemPoints*100).toInt()){
        in -10000..49 -> Color.Red
        in 50..69 -> Color.Yellow
        in 70..85 -> colorResource(id = R.color.light_green)
        else -> Color.Green
    }
    Card(
        shape = RoundedCornerShape(50),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = modifier.size(35.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if(userPoints.toInt() == -2 || systemPoints == 0.0 || isBonus) MaterialTheme.colors.secondary else outlineColor
        )
    ){
        Text(
            text =
            if (userPoints.toInt() >= 0) if(userPoints.toInt().toDouble() == userPoints) userPoints.toInt().toString() else ((userPoints*10.0).toInt()/10.0).toString()
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
                modifier = Modifier.size(150.dp)
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
    Column {
        Divider()
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
                icon = {
                    Icon(
                        painterResource(id = scheduleItem.icon),
                        contentDescription = scheduleItem.title,
                        modifier = Modifier.size(30.dp)
                    )
                },
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
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier.size(30.dp)
                        )
                    },
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
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                )
            }
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
        elevation = if (MaterialTheme.colors.surface == Color.White) 4.dp else 0.dp,
        onClick = onClick,
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp,vertical = 3.dp)
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

@Composable
fun SwitchButton(
    isOn: Boolean = false,
    onChange: (Boolean) -> Unit = {},
    @StringRes text: Int = R.string.not_specified,
    icon: Int = R.drawable.admin_button,
){
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.defaultMinSize(minHeight = 72.dp)) {
        Spacer(modifier = Modifier.size(16.dp))
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colors.secondary,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = stringResource(text),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .weight(1f),
            fontSize = 16.sp
        )
        Switch(
            checked = isOn,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colors.secondary,
                uncheckedTrackColor = MaterialTheme.colors.primary,
                checkedThumbColor = MaterialTheme.colors.secondary,
                uncheckedThumbColor = MaterialTheme.colors.secondary
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun ErrorDisplayText(errorMessage:String, modifier: Modifier = Modifier){
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            stringResource(R.string.error_message),
            color = MaterialTheme.colors.error,
            fontWeight = FontWeight.Bold
        )
        Text(errorMessage, textAlign = TextAlign.Center)
    }
}


val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Уведомления об успеваемости"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Показывает уведомления каждый раз, когда меняется оценка студента"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

fun makeStatusNotification(message: String, head: String, context: Context, link: String = "") {

    val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
    val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(CHANNEL_ID, name, importance)
    channel.description = description

    val resultIntent =
        if(link.isBlank()) Intent(Intent.ACTION_VIEW, Uri.parse("https://BetterOrioks.com/AcademicPerformance"), context, MainActivity::class.java)
    else if(link == "schedule") Intent(Intent.ACTION_VIEW,Uri.parse("https://BetterOrioks.com"),context,MainActivity::class.java)
    else Intent(Intent.ACTION_VIEW, Uri.parse(link))

    val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(resultIntent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(head)
        .setContentIntent(resultPendingIntent)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(1) { it.toLong() })
        .setAutoCancel(true)

    // Show the notification
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }else {
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}