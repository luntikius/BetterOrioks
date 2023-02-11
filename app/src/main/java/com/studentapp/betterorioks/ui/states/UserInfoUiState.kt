package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.UserInfo

sealed interface UserInfoUiState {
    object NotStarted: UserInfoUiState
    object Loading: UserInfoUiState
    object Error: UserInfoUiState
    data class Success (val userInfo: UserInfo): UserInfoUiState
}