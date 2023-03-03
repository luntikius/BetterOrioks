package com.studentapp.betterorioks.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Keep
@Serializable
data class Token(
    @SerialName("token")
    val token: String = "",
    @SerialName("error")
    val error: String = ""
)
