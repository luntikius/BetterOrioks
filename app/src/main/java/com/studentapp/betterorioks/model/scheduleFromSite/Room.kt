package com.studentapp.betterorioks.model.scheduleFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class Room(
    @SerialName("Code")
    val code: Int,
    @SerialName("Name")
    val name: String
)
