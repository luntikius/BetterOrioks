package com.studentapp.betterorioks.ui.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.data.AdminIds
import com.studentapp.betterorioks.model.News
import com.studentapp.betterorioks.model.UserInfo
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.components.*
import com.studentapp.betterorioks.ui.states.NewsUiState
import com.studentapp.betterorioks.ui.states.UserInfoUiState

@Composable
fun ProfileCardContent(userInfo: UserInfo) {
    Row(modifier = Modifier.padding(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = userInfo.fullName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${userInfo.recordBookId} · ${userInfo.group}",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ProfileCard(uiState: AppUiState){
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp,vertical = 3.dp)
    ) {
        when (uiState.userInfoUiState) {
            is UserInfoUiState.Success ->
                ProfileCardContent(userInfo = (uiState.userInfoUiState as UserInfoUiState.Success).userInfo)
            is UserInfoUiState.Loading ->
                LoadingScreen()
            else -> ErrorScreen()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExitButton(onExitClick: () -> Unit){
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.error,
        elevation = 5.dp,
        onClick = onExitClick,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp,vertical = 3.dp)
            .defaultMinSize(minHeight = 72.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.exit),
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.logout),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun News(
    uiState: AppUiState
){
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.newspaper),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    stringResource(R.string.latest_news),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            when (uiState.newsUiState) {
                is NewsUiState.Success -> {
                    NewsContent(news = uiState.newsUiState.news)
                }
                is NewsUiState.Error   -> {
                    ErrorScreen(modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth())
                }
                else                   -> {
                    LoadingScreen(modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun NewsContent(news: List<News>){
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(bottom = 4.dp)
    ){
        news.forEach{
            val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://orioks.miet.ru/${it.link}")) }
            Divider()
            Row(modifier = Modifier
                .clickable { context.startActivity(intent) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    it.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp)
                )
                Icon(painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ThemeSelectorButton(
    onChange: (Int) -> Unit = {},
    icon: Int = R.drawable.dark_mode,
    text: Int = R.string.theme,
    items: List<String>,
    currentSelectedId: Int
){
    Card(
        shape = RoundedCornerShape(size = 16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp,vertical = 3.dp)
            .defaultMinSize(minHeight = 72.dp)
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically){
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
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Row(modifier = Modifier.clickable { expanded = true }, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    items[currentSelectedId],
                )
                Icon(
                    painterResource(
                        id = R.drawable.expand_more
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colors.primary
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },

                    ){
                    items.forEachIndexed { index,s ->
                        DropdownMenuItem(onClick = {
                            onChange(index)
                            expanded = false
                        }) {
                            Text(
                                s,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp,vertical = 4.dp)
                                    .wrapContentSize(Alignment.Center)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

val themeItems = listOf(
    "Системная","Светлая", "Тёмная"
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    onExitClick: () -> Unit = {},
    uiState: AppUiState = AppUiState(),
    viewModel: BetterOrioksViewModel
){
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+YQD5-csbrqk4ZjEy")) }
    val pullRefreshState = rememberPullRefreshState((uiState.userInfoUiState == UserInfoUiState.Loading), {
        viewModel.getUserInfo(refresh = true)
        viewModel.getNews()
    })
    Box (modifier = Modifier
        .pullRefresh(pullRefreshState)
        .fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Spacer(modifier = Modifier.size(16.dp))
                ProfileCard(uiState = uiState)
                val id = if(uiState.userInfoUiState is UserInfoUiState.Success) (uiState.userInfoUiState as UserInfoUiState.Success).userInfo.recordBookId else 0
                if (id.toString() in AdminIds.ids) {
                    Spacer(modifier = Modifier.size(8.dp))
                    AnyButton(
                        text = R.string.run_test,
                        icon = R.drawable.admin_button,
                        onClick = { viewModel.test(context) })
                }
                Spacer(modifier = Modifier.size(8.dp))
                News(uiState = uiState)
                Spacer(modifier = Modifier.size(8.dp))
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        viewModel.changeNotificationState(switchValue = true)
                    } else {
                        Toast.makeText(context, R.string.notifications_permission_required, Toast.LENGTH_SHORT).show()
                    }
                }
                SwitchButton(
                    isOn = uiState.sendNotifications,
                    onChange = {
                        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            }else{
                                Toast.makeText(context,"Приложению необходим доступ к уведомлениям",Toast.LENGTH_SHORT).show()
                            }
                        }else {
                            viewModel.changeNotificationState(switchValue = it)
                        } },
                    text = R.string.notifications,
                    icon = R.drawable.notifications,
                )
                Spacer(modifier = Modifier.size(8.dp))
                ThemeSelectorButton(
                    onChange = {viewModel.setTheme(it)},
                    items = themeItems,
                    currentSelectedId = uiState.theme
                )
            }
            item{
                Spacer(modifier = Modifier.size(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    onClick = {context.startActivity(intent)},
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 5.dp,
                    border = BorderStroke(width = 1.dp,color = MaterialTheme.colors.secondary),
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp,vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = R.drawable.telegram), contentDescription = null, modifier = Modifier
                            .size(64.dp)
                            .padding(start = 16.dp))
                        Text(
                            text = "Телеграм канал приложения с новостями и отзывами",
                            color = MaterialTheme.colors.secondary,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
            item{
                ExitButton(onExitClick = onExitClick)
            }
        }
        PullRefreshIndicator(
            refreshing = uiState.userInfoUiState is UserInfoUiState.Loading,
            state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.secondary
        )
    }

}