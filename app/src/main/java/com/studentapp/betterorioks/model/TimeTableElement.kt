package com.studentapp.betterorioks.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class TimeTableElement(
    @SerialName("1")
    val time1: List<String>,
    @SerialName("2")
    val time2: List<String>,
    @SerialName("3")
    val time3: List<String>,
    @SerialName("4")
    val time4: List<String>,
    @SerialName("5")
    val time5: List<String>,
    @SerialName("6")
    val time6: List<String>,
    @SerialName("7")
    val time7: List<String>
){
    val times = listOf(time1,time2,time3,time4,time5,time6,time7)
}
