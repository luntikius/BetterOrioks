package com.studentapp.betterorioks.ui.states

enum class AuthState {
    LoggedIn,
    Loading,
    BadLoginOrPassword,
    TokenLimitReached,
    UnexpectedError,
    NotLoggedIn
}