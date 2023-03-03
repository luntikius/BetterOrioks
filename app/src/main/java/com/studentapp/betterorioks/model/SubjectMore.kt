package com.studentapp.betterorioks.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
data class SubjectMore (
    @SerialName("alias")
    val alias: String = "",
    @SerialName("current_grade")
    val currentGrade: Double = -2.0,
    @SerialName("max_grade")
    val maxGrade: Double = 0.0,
    @SerialName("name")
    val name: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("week")
    val week: Int = 0
    )
