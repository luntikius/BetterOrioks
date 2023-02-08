package com.example.betterorioks.model

@kotlinx.serialization.Serializable
data class UserInfo(
    val course: Int = 0,
    val department: String = "",
    val full_name: String = "",
    val group: String = "",
    val record_book_id: Int = 0,
    val semester: Int = 0,
    val study_direction: String = "",
    val study_profile: String = "",
    val year: String = ""
)
