package com.example.betterorioks.model

@kotlinx.serialization.Serializable
data class Exit (
    val info: String = "",
    val error: String = ""
)