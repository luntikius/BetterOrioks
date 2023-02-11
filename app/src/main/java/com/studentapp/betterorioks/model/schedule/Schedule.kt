package com.studentapp.betterorioks.model.schedule

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Schedule(
    val name: String, //Название
    val type: String, //Тип пары
    val day: Int = -1, //Номер на неделе
    @SerialName("class")
    val clas: Int = -1, //Номер пары в дне
    val week:Int = -1, //Тип недели
    val week_recurrence:Int = 4, //Загадка
    val location: String = "", //Номер аудитории
    val teacher: String = "" //Учитель

)
