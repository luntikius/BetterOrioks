package com.studentapp.betterorioks.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
@Keep
data class AcademicDebt(
    @SerialName("consultation_schedule")
    val consultationSchedule: String = "",
    @SerialName("control_form")
    val controlForm: String = "",
    @SerialName("current_grade")
    val currentGrade: Double = -2.0,
    @SerialName("deadline")
    val deadline: String = "",
    @SerialName("department")
    val department: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("max_grade")
    val maxGrade: Double = -2.0,
    @SerialName("name")
    val name: String = "",
    @SerialName("teachers")
    val teachers: List<String> = listOf()
)
