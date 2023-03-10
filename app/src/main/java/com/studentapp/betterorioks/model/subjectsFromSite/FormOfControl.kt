package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class FormOfControl(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = ""
)
