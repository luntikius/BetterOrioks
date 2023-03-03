package com.studentapp.betterorioks.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Keep
@Serializable
data class Subject(
    @SerialName("control_form")
    val controlForm: String = "",
    @SerialName("current_grade")
    val currentGrade: Double = 0.0,
    @SerialName("department")
    val department: String = "",
    @SerialName("exam_date")
    val examDate: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("max_grade")
    val maxGrade: Double = 0.0,
    @SerialName("name")
    val name: String = "",
    @SerialName("teachers")
    val teachers: List<String> = listOf()
)
