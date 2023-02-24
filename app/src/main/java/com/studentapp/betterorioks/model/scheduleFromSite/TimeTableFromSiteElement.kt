package com.studentapp.betterorioks.model.scheduleFromSite

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class TimeTableFromSiteElement(
    @SerialName("Time")
    val time: String,
    @SerialName("Code")
    val code: Int,
    @SerialName("TimeFrom")
    val timeFrom: String,
    @SerialName("TimeTo")
    val timeTo: String
){
    private val start = timeFrom.slice(timeFrom.indexOf("T")+1 .. timeFrom.indexOf("T")+5)
    private val end = timeTo.slice(timeTo.indexOf("T")+1 .. timeTo.indexOf("T")+5)
    val timeString = "$start - $end"
    val dayOrder = time.filter { it in "0123456789" }.toInt()
}
