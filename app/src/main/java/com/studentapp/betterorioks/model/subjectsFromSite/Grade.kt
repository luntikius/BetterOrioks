package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class Grade(
    @SerialName("b")
    val score: String = "",
    @SerialName("f")
    val fullScore: String = "-",
    @SerialName("p")
    val percent: String = "",
    @SerialName("o")
    val mark: String = "",
    @SerialName("w")
    val words: String = ""
)
