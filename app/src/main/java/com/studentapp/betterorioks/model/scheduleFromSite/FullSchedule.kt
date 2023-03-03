package com.studentapp.betterorioks.model.scheduleFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class FullSchedule(
    @SerialName("Times")
    val timeTable: List<TimeTableFromSiteElement> = listOf(),
    @SerialName("Data")
    val schedule: List<ScheduleFromSiteElement> = listOf(),
    @SerialName("Semestr")
    val semester: String = ""
)