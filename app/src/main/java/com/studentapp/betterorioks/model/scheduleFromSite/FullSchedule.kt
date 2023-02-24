package com.studentapp.betterorioks.model.scheduleFromSite

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FullSchedule(
    @SerialName("Times")
    val timeTable: List<TimeTableFromSiteElement> = listOf(),
    @SerialName("Data")
    val schedule: List<ScheduleFromSiteElement> = listOf(),
    @SerialName("Semestr")
    val semester: String = ""
)