package com.studentapp.betterorioks.model.scheduleFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class ScheduleFromSiteElement(
    @SerialName("Day")
    val day: Int,
    @SerialName("DayNumber")
    val dayNumber:Int,
    @SerialName("Time")
    val time: TimeTableFromSiteElement,
    @SerialName("Class")
    val subject: ScheduleFromSiteSubject,
    @SerialName("Group")
    val group: Group,
    @SerialName("Room")
    val room: Room
)
