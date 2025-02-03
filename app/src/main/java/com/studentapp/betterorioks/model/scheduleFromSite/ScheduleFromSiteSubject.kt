package com.studentapp.betterorioks.model.scheduleFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
class ScheduleFromSiteSubject(
    @SerialName("Name")
    val name: String,
    @SerialName("TeacherFull")
    val teacherFull: String,
    @SerialName("Teacher")
    val teacher: String,
){
    val formFromString =
        if (name.contains("["))
            name.slice(name.indexOf("[")+1 until name.indexOf("]"))
        else "Н/С"

    val nameFromString =
        if (name.contains("["))
            name.slice(0 until name.indexOf("["))
        else name
}
