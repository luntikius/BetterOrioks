package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
data class DebtControlEvent(
    @SerialName("date_1")
    val date1: String = "",
    @SerialName("date_2")
    val date2: String = "",
    @SerialName("time_1")
    val time1: String = "",
    @SerialName("time_2")
    val time2: String = "",
    @SerialName("aud_1")
    val room1: String = "",
    @SerialName("aud_2")
    val room2: String = ""
)
