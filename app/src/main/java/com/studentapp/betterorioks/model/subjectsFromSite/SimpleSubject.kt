package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SimpleSubject(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val systemId: Int = 0,
    val userScore: String = "",
    val isSubject: Boolean = true
)
