package com.studentapp.betterorioks.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.model.UserInfo
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.components.AnyButton
import com.studentapp.betterorioks.ui.components.ErrorScreen
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.studentapp.betterorioks.ui.states.UserInfoUiState

@Composable
fun ProfileCardContent(userInfo: UserInfo){
    Row(modifier = Modifier.padding(16.dp)){
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = userInfo.fullName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = stringResource(R.string.student_number))
                Spacer(modifier = Modifier.width(32.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = userInfo.recordBookId.toString(), textAlign = TextAlign.End)
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = stringResource(R.string.group))
                Spacer(modifier = Modifier.width(32.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = userInfo.group, textAlign = TextAlign.End)
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = stringResource(R.string.direction))
                Spacer(modifier = Modifier.width(32.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = userInfo.studyDirection, textAlign = TextAlign.End)
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = stringResource(R.string.institute))
                Spacer(modifier = Modifier.width(32.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = userInfo.department, textAlign = TextAlign.End)
            }

        }
    }
}

@Composable
fun ProfileCard(uiState: AppUiState){
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp)
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
            .padding(horizontal = 16.dp, vertical = 3.dp)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    onExitClick: () -> Unit = {},
    onDebtClick: () -> Unit = {},
    uiState: AppUiState = AppUiState(),
    viewModel: BetterOrioksViewModel
){
    val pullRefreshState = rememberPullRefreshState((uiState.userInfoUiState == UserInfoUiState.Loading), { viewModel.getUserInfo(refresh = true) })
    Box (modifier = Modifier
        .pullRefresh(pullRefreshState)
        .fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Spacer(modifier = Modifier.size(16.dp))
                ProfileCard(uiState = uiState)
                Spacer(modifier = Modifier.size(8.dp))
                AnyButton(onClick = onDebtClick, text = R.string.Debts, icon = R.drawable.debt)
                //Spacer(modifier = Modifier.size(8.dp))
                //AnyButton(text = R.string.app_name, icon = R.drawable.visibility_on, onClick = {viewModel.getFullSchedule()})
            }
            //temp
            item{
                val clipboardManager = LocalClipboardManager.current
                Spacer(modifier = Modifier.size(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 5.dp,
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 3.dp)
                        .clickable {
                            clipboardManager.setText(AnnotatedString("BetterOrioksApp@yandex.ru"))
                        }
                ) {
                    Text(
                        text = "Это тестовая версия приложения, если вы нашли баг, пожалуйста сообщите о нем. \nНаша почта: BetterOrioksApp@yandex.ru \n(нажмите чтобы скопировать)",
                        color = MaterialTheme.colors.primary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
            item{
                ExitButton(onExitClick = onExitClick)
            }
        }
        PullRefreshIndicator(
            refreshing = uiState.isAcademicPerformanceRefreshing,
            state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.secondary
        )
    }

}