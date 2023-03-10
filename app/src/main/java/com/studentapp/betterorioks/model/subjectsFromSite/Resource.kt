package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
data class Resource (
    @SerialName("id")
    val id: Int = 0,
    @SerialName("id_km")
    val idKm: Int = 0,
    @SerialName("is_test")
    val isTest: Boolean = false,
    @SerialName("name")
    val name: String = "",
    @SerialName("link")
    val link: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("label")
    val label: String = ""
)