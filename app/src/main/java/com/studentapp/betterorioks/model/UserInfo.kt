package com.studentapp.betterorioks.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
data class UserInfo(
    @SerialName("course")
    val course: Int = 0,
    @SerialName("department")
    val department: String = "",
    @SerialName("full_name")
    val fullName: String = "",
    @SerialName("group")
    val group: String = "",
    @SerialName("record_book_id")
    val recordBookId: Int = 0,
    @SerialName("semester")
    val semester: Int = 0,
    @SerialName("study_direction")
    val studyDirection: String = "",
    @SerialName("study_profile")
    val studyProfile: String = "",
    @SerialName("year")
    val year: String = ""
)
