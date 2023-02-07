package com.example.betterorioks.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val token: String = "",
    val error: String = ""
)
