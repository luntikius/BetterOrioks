package com.studentapp.betterorioks.model.subjectsFromSite

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
@Keep
@kotlinx.serialization.Serializable
class SubjectFromSite(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("id_science")
    val scienceId: Int = 0,
    @SerialName("id_form_control")
    val formOfControlId: Int = 0,
    @SerialName("id_dis")
    val disciplineId: Int = 0,
    @SerialName("segments")
    val segments: List<Segment> = listOf(),
    @SerialName("formControl")
    val formOfControl: FormOfControl = FormOfControl(),
    @SerialName("preps")
    val teachers: List<Teacher> = listOf(),
    @SerialName("grade")
    val grade: Grade = Grade(),
    @SerialName("date_exam")
    val examDate: String = "",
    @SerialName("time_exam")
    val examTime: String = "",
    @SerialName("room_exam")
    val examRoom: String = "",
    @SerialName("debt")
    val isDebt: Boolean = false,
    @SerialName("mvb")
    val maxAvailableScore: Double = 0.0,
    @SerialName("is_exam_time")
    val isExamTime: Boolean = false,
    @SerialName("debtKms")
    val debtControlEvents: List<DebtControlEvent> = listOf()
) {
    fun getControlEvents():List<ControlEvent>{
        val controlEvents = mutableListOf<ControlEvent>()
        segments.forEach { controlEvents.addAll(it.allControlEvents) }
        return controlEvents
    }
    fun getMaxScore():Double{
        val controlEvents = getControlEvents()
        var maxScore = 0.0
        controlEvents.forEach {
            if (it.grade.score != "-" && it.bonus == 0) maxScore += it.maxScore
        }
        return maxScore
    }
}
