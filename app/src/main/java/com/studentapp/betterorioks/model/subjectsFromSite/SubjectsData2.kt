package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
class SubjectsData2 {
    @SerialName("dises")
    val subjects: Map<String,SubjectFromSite> = mapOf()
}