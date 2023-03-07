package com.studentapp.betterorioks.model.subjectsFromSite

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Segment(
    @SerialName("allKms")
    val allControlEvents: List<ControlEvent> = listOf()
)
