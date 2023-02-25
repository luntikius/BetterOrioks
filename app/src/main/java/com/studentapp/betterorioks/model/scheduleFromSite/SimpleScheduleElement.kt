package com.studentapp.betterorioks.model.scheduleFromSite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class SimpleScheduleElement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
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
