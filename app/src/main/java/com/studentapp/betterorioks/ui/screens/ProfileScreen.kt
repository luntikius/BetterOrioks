package com.studentapp.betterorioks.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.window.DialogProperties
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
fun SocialNetworkButton(name: String, link: String, icon: Int){
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(link)) }
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp,vertical = 8.dp)
            .clickable { context.startActivity(intent) }){
        Spacer(modifier = Modifier.size(16.dp))
        Icon(painter = painterResource(id = icon), tint = MaterialTheme.colors.secondary , contentDescription = null, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            name,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Icon(painter = painterResource(id = R.drawable.arrow_outward_fill0_wght400_grad0_opsz48), contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colors.primary)
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ThemeSelectorButton(
    onChange: (Int) -> Unit = {},
    icon: Int = R.drawable.dark_mode,
    text: Int = R.string.theme,
    items: List<String>,
    currentSelectedId: Int
){
    var expanded by rememberSaveable { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { expanded = !expanded }){
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
                .weight(1f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                items[currentSelectedId],
                color = MaterialTheme.colors.secondary,
            )
            if(expanded) {
                AlertDialog(
                    onDismissRequest = { expanded = false },
                    text = {
                        Column() {
                            Text(
                                stringResource(id = R.string.theme),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.onSurface
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            items.forEachIndexed { index,s ->
                                Row(verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        onChange(index)
                                        expanded = false
                                    }
                                ) {
                                    RadioButton(
                                        selected = currentSelectedId == index,
                                        onClick = {
                                            onChange(index)
                                            expanded = false
                                        }
                                    )
                                    Text(
                                        text = s,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                            .fillMaxWidth(),
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colors.onSurface
                                    )
                                }
                            }
                        }
                    },
                    buttons = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            TextButton(
                                onClick = { expanded = false },
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
                    properties = DialogProperties(
                        dismissOnClickOutside = true
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun ExitAlert(isAlert: Boolean, onDismiss: () -> Unit, onExit: () -> Unit){
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
                        stringResource(R.string.exit_alert_text),
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                } },
            buttons = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    TextButton(
                        onClick = {
                            onExit()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(end = 8.dp,bottom = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.continue_string),
                            color = MaterialTheme.colors.secondary,
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

val themeItems = listOf(
    "Системная","Светлая", "Тёмная"
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    onExitClick: () -> Unit = {},
    uiState: AppUiState = AppUiState(),
    viewModel: BetterOrioksViewModel,
    onSettingsClick: () -> Unit
){
    val context = LocalContext.current

    val pullRefreshState = rememberPullRefreshState((uiState.userInfoUiState == UserInfoUiState.Loading), {
        viewModel.getUserInfo(refresh = true)
        viewModel.getNews()
    })

    var isAlert by remember {
        mutableStateOf(false)
    }

    Box (modifier = Modifier
        .pullRefresh(pullRefreshState)
        .fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {

                ProfileCard(uiState = uiState)

                val id = if(uiState.userInfoUiState is UserInfoUiState.Success) (uiState.userInfoUiState as UserInfoUiState.Success).userInfo.recordBookId else 0

                if (id.toString() in AdminIds.ids) {
                    Spacer(modifier = Modifier.size(8.dp))
                    AnyButton(
                        text = R.string.run_test,
                        icon = R.drawable.admin_button,
                        onClick = { viewModel.test(context = context) })
                }
                
                Spacer(modifier = Modifier.size(16.dp))
                
                ExitAlert(
                    isAlert = isAlert,
                    onDismiss = { isAlert = false },
                    onExit = onExitClick
                )

                News(uiState = uiState)

                Spacer(modifier = Modifier.size(16.dp))

                Divider(modifier = Modifier.padding(horizontal = 32.dp), color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.size(4.dp))

                SocialNetworkButton(name = "Telegram канал приложения",link = "https://t.me/+YQD5-csbrqk4ZjEy" ,icon = R.drawable.telegram)

                SocialNetworkButton(name = "GitHub репозиторий приложения",link = "https://github.com/luntikius/BetterOrioks",icon = R.drawable.github)

                Spacer(modifier = Modifier.size(4.dp))

                Divider(modifier = Modifier.padding(horizontal = 32.dp), color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.size(16.dp))

                AnyButton(text = R.string.settings,icon = R.drawable.setting, onClick = onSettingsClick)

                Spacer(modifier = Modifier.size(8.dp))

                ExitButton(onExitClick = {isAlert = true})

            }
        }
        PullRefreshIndicator(
            refreshing = uiState.userInfoUiState is UserInfoUiState.Loading,
            state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.secondary
        )
    }

}