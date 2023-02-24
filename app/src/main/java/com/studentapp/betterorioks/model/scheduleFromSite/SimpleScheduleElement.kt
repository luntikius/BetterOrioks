package com.studentapp.betterorioks.model.scheduleFromSite

data class SimpleScheduleElement(
    val day: Int = 0,
    val number: Int = 0,
    val times: String = "",
    val type: String = "Пара",
    val name: String = "",
    val teacher: String = "",
    val room: String = "",
    val isWindow: Boolean = false,
    val windowDuration: String = ""
)
