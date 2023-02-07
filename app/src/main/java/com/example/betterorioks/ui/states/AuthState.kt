package com.example.betterorioks.ui.states

enum class AuthState {
    LoggedIn,
    Loading,
    BadLoginOrPassword,
    TokenLimitReached,
    UnexpectedError,
    NotLoggedIn
}