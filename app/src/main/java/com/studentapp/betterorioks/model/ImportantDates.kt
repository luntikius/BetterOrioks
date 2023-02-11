package com.studentapp.betterorioks.model

@kotlinx.serialization.Serializable
data class ImportantDates(
    val semester_start: String ="",
    val session_start: String = "",
    val session_end: String = "",
    val next_semester_start: String = ""
)
