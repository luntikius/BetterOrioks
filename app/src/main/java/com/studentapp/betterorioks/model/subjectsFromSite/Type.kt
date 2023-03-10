package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class Type(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("sh")
    val shortName: String = "",
    @SerialName("important")
    val important: String = "",
    @SerialName("sl")
    val shortEnglishName: String = ""
)
