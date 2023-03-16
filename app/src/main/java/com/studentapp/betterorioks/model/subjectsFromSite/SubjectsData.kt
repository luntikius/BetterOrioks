package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
class SubjectsData (
    @SerialName("dises")
    val subjects: List<SubjectFromSite> = listOf(),
)
