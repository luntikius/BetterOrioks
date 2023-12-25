package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

//"id": 24,
//"year": 2021,
//"sem": 1,
//"date_start": "30-08-2021",
//"session_start": null,
//"session_end": null,
//"name": "2021 - 2022 год, 1 семестр"

@Keep
@kotlinx.serialization.Serializable
data class Semester(
    @SerialName("id")
    val id: Int = 1,
    @SerialName("year")
    val year: Int = 2023,
    @SerialName("sem")
    val semesterNumber: Int = 1,
    @SerialName("date_start")
    val dateStart: String = "",
    @SerialName("name")
    val name: String = ""
)
