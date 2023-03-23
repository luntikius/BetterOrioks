package com.studentapp.betterorioks.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.components.SwitchButton


@Composable
fun NotificationSwitch(context: Context, onClick: (Boolean) -> Unit, state: Boolean, icon: Int, @StringRes text: Int){
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onClick(true)
        } else {
            Toast.makeText(context, R.string.notifications_permission_required, Toast.LENGTH_SHORT).show()
        }
    }
    SwitchButton(
        isOn = state,
        onChange = {
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }else{
                    Toast.makeText(
                        context,
                        R.string.notifications_permission_required,
                        Toast.LENGTH_SHORT).show()
                }
            }else {
                onClick(it)
            } },
        text = text,
        icon = icon,
    )
}

@Composable
fun SettingsScreen(uiState: AppUiState, viewModel: BetterOrioksViewModel, onBackButtonPress: () -> Unit){
    val context = LocalContext.current
    Scaffold(
        topBar = { TopBar(name = stringResource(id = R.string.settings), onClick = onBackButtonPress) }
    ) {paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            Text(stringResource(R.string.notifications_text), fontSize = 14.sp, color = MaterialTheme.colors.secondary, modifier = Modifier.padding(8.dp))
            NotificationSwitch(
                context = context,
                onClick = {viewModel.changeNotificationState(it)},
                state = uiState.sendNotifications,
                text = R.string.notifications,
                icon = R.drawable.notifications
            )
            Divider()
            NotificationSwitch(
                context = context,
                onClick = {viewModel.changeNewsNotificationState(it)},
                state = uiState.sendNewsNotifications,
                icon = R.drawable.news_notifications,
                text = R.string.news_notifications
            )
            Divider()
            Text(stringResource(R.string.other), fontSize = 14.sp, color = MaterialTheme.colors.secondary, modifier = Modifier.padding(8.dp))
            ThemeSelectorButton(
                onChange = {viewModel.setTheme(it)},
                items = themeItems,
                currentSelectedId = uiState.theme
            )
            Divider()
        }
    }
}