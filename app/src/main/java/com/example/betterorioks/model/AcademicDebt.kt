package com.example.betterorioks.model

data class AcademicDebt(
    val consultation_schedule: String = "",
    val control_form: String = "",
    val current_grade: Double = -2.0,
    val deadline: String = "",
    val department: String = "",
    val id: Int = 0,
    val max_grade: Double = -2.0,
    val name: String = "",
    val teachers: List<String> = listOf()
)
