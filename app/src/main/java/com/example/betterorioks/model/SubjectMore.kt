package com.example.betterorioks.model

@kotlinx.serialization.Serializable
data class SubjectMore (
    val alias: String,
    val current_grade: Double,
    val max_grade: Double,
    val name: String,
    val type: String,
    val week: Int
    )
