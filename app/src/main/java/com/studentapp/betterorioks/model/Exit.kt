package com.studentapp.betterorioks.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
data class Exit (
    @SerialName("info")
    val info: String = ""
)