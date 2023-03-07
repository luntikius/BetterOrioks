package com.studentapp.betterorioks.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studentapp.betterorioks.R
import com.studentapp.betterorioks.ui.AppUiState
import com.studentapp.betterorioks.ui.states.AuthState
import com.studentapp.betterorioks.ui.theme.BetterOrioksTheme

@Composable
fun AuthenticationTextField(
    onValueChange: (String) -> Unit = {},
    value: String = "",
    label: String = "",
    isError: Boolean
){
    OutlinedTextField(
        shape = RoundedCornerShape(16.dp),
        value = value,
        onValueChange = { onValueChange(it) },
        label = {Text(text = label)},
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.padding(horizontal = 16.dp),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onBackground,
            focusedIndicatorColor = MaterialTheme.colors.secondary,
            unfocusedIndicatorColor = MaterialTheme.colors.secondary.copy(),
            backgroundColor = MaterialTheme.colors.primaryVariant
        )

    )
}

@Composable
fun AuthenticationPasswordTextField(
    onValueChange: (String) -> Unit = {},
    value: String = "",
    label: String = "",
    onButton: () -> Unit,
    isError: Boolean
){
    val showPassword = remember { mutableStateOf(false) }
    OutlinedTextField(
        shape = RoundedCornerShape(16.dp),
        value = value,
        onValueChange = { onValueChange(it) },
        label = {Text(text = label)},
        modifier = Modifier.padding(horizontal = 16.dp),
        singleLine = true,
        isError = isError,
        keyboardActions = KeyboardActions {onButton()},
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if(showPassword.value) R.drawable.visibility_on else R.drawable.visibility_off
            IconButton(onClick = { showPassword.value = !showPassword.value }) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(R.string.visibility),
                    tint = if (!isError) MaterialTheme.colors.secondary else MaterialTheme.colors.error,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp)
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onBackground,
            focusedIndicatorColor = MaterialTheme.colors.secondary,
            unfocusedIndicatorColor = MaterialTheme.colors.secondary.copy(),
            backgroundColor = MaterialTheme.colors.primaryVariant
        )
    )
}

@Composable
fun AuthorizationScreen(
    onLogin: (String,String) -> Unit,
    uiState: AppUiState = AppUiState()
){
    BetterOrioksTheme{
        var login by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        val focusManager = LocalFocusManager.current
        val isError = !(uiState.authState == AuthState.NotLoggedIn || uiState.authState == AuthState.LoggedIn)
        @StringRes
        val errorText = when(uiState.authState){
            AuthState.UnexpectedError -> R.string.unexpected_error
            AuthState.TokenLimitReached -> R.string.token_limit_reached
            else -> R.string.bad_login_or_password
            }

        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentSize(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(250.dp)
                )
                Text(
                    text = if(!isError) stringResource(R.string.text_on_login) else stringResource(id = errorText),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    color = if(!isError) MaterialTheme.colors.onBackground else MaterialTheme.colors.error
                )
                AuthenticationTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = stringResource(R.string.login),
                    isError = isError
                )
                Spacer(modifier = Modifier.size(8.dp))
                AuthenticationPasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.password),
                    onButton = {
                        focusManager.clearFocus()
                    },
                    isError = isError
                )
                Button(
                    onClick = {
                        onLogin(login, password)
                        password = ""
                        login = ""
                              },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant,
                        contentColor = MaterialTheme.colors.secondary ,
                        disabledContentColor = MaterialTheme.colors.secondary.copy(0.4f)
                    ),
                    modifier = Modifier.padding(16.dp),
                    enabled = (login != "" && password != ""),
                    elevation = ButtonDefaults.elevation(10.dp)
                ) {
                    Row{
                        Text(
                            text = stringResource(R.string.LogIn),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.size(40.dp))
            }
        }
    }
}