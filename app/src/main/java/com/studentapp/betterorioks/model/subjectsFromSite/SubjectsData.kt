package com.studentapp.betterorioks.model.subjectsFromSite

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SubjectsData (
    @SerialName("dises")
    val subjects: List<SubjectFromSite> = listOf()
)