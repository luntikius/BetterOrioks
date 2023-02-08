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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.betterorioks.R
import com.example.betterorioks.model.UserInfo
import com.example.betterorioks.ui.AppUiState
import com.example.betterorioks.ui.components.AnyButton
import com.example.betterorioks.ui.components.ErrorScreen
import com.example.betterorioks.ui.components.LoadingScreen
import com.example.betterorioks.ui.states.UserInfoUiState

@Composable
fun ProfileCardContent(userInfo: UserInfo){
    Row(modifier = Modifier.padding(16.dp)){
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = userInfo.full_name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = "Номер студенческого билета: ")
                Spacer(modifier = Modifier.width(32.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = userInfo.record_book_id.toString(), textAlign = TextAlign.End)
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = "Группа: ")
                Spacer(modifier = Modifier.width(32.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = userInfo.group, textAlign = TextAlign.End)
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = "Направление: ")
                Spacer(modifier = Modifier.width(32.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = userInfo.study_direction, textAlign = TextAlign.End)
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = "Институт: ")
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
        elevation = 10.dp,
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
fun ProfileScreen(
    onExitClick: () -> Unit = {},
    onDebtClick: () -> Unit = {},
    uiState: AppUiState = AppUiState()
){
    Column() {
        Spacer(modifier = Modifier.size(16.dp))
        ProfileCard(uiState = uiState)
        Spacer(modifier = Modifier.size(8.dp))
        AnyButton(onClick = onDebtClick, text = R.string.Debts, icon = R.drawable.debt)
        Spacer(modifier = Modifier.weight(1f))
        ExitButton (onExitClick = onExitClick)
        Spacer(modifier = Modifier.size(16.dp))
    }
}