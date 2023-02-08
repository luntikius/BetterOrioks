package com.example.betterorioks.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterorioks.R
import com.example.betterorioks.model.UserInfo
import com.example.betterorioks.ui.AppUiState
import com.example.betterorioks.ui.components.ErrorScreen
import com.example.betterorioks.ui.components.LoadingScreen
import com.example.betterorioks.ui.states.UserInfoUiState

@Composable
fun ProfileCardContent(userInfo: UserInfo){
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)){
        Column() {
            Text(text = userInfo.group)
            Text(text = userInfo.full_name)
            Text(text = userInfo.study_direction)
        }
    }
}

@Composable
fun ProfileCard(uiState: AppUiState){
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 10.dp,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .defaultMinSize(minHeight = 213.dp)
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
        elevation = 10.dp,
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
                fontSize = 20.sp
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
    uiState: AppUiState = AppUiState()
){
    Column() {
        Spacer(modifier = Modifier.size(16.dp))
        ProfileCard(uiState = uiState)
        Spacer(modifier = Modifier.weight(1f))
        ExitButton (onExitClick = onExitClick)
        Spacer(modifier = Modifier.size(16.dp))
    }
}