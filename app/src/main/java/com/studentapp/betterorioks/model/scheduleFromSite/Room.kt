package com.studentapp.betterorioks.model.scheduleFromSite

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Room(
    @SerialName("Code")
    val code: Int,
    @SerialName("Name")
    val name: String
)
