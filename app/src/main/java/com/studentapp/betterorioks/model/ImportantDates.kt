package com.studentapp.betterorioks.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
data class ImportantDates(
    @SerialName("semester_start")
    val semesterStart: String ="",
    @SerialName("session_start")
    val sessionStart: String = "",
    @SerialName("session_end")
    val sessionEnd: String = "",
    @SerialName("next_semester_start")
    val nextSemesterStart: String = ""
)
