package com.studentapp.betterorioks.model.scheduleFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class Group(
    @SerialName("Code")
    val code: String,
    @SerialName("Name")
    val name: String
)
