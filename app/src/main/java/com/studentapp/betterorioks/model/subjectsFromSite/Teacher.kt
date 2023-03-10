package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class Teacher(
    @SerialName("login")
    val login: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("email")
    val email: String = ""
)
