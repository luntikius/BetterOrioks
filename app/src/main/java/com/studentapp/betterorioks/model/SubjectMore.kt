package com.studentapp.betterorioks.model

@kotlinx.serialization.Serializable
data class SubjectMore (
    val alias: String = "",
    val current_grade: Double = -2.0,
    val max_grade: Double = 0.0,
    val name: String = "",
    val type: String = "",
    val week: Int = 0
    )
