package com.example.betterorioks.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val control_form: String = "",
    val current_grade: Double = 0.0,
    val department: String = "",
    val exam_date: String = "",
    val id: Int = 0,
    val max_grade: Double = 0.0,
    val name: String = "",
    val teachers: List<String> = listOf()
)
