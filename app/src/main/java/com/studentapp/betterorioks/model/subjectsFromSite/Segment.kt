package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class Segment(
    @SerialName("allKms")
    val allControlEvents: List<ControlEvent> = listOf()
)
