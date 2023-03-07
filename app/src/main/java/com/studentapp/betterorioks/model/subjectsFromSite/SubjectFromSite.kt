package com.studentapp.betterorioks.model.subjectsFromSite

import kotlinx.serialization.SerialName

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
    val maxAvailableScore: Int = 0,
    @SerialName("is_exam_time")
    val isExamTime: Boolean = false
) {
    fun getControlEvents():List<ControlEvent>{
        val controlEvents = mutableListOf<ControlEvent>()
        segments.forEach { controlEvents.addAll(it.allControlEvents) }
        return controlEvents
    }
    fun getMaxScore():Int{
        val controlEvents = getControlEvents()
        var maxScore = 0
        controlEvents.forEach {
            if (it.grade.score != "-") maxScore += it.maxScore
        }
        return maxScore
    }
}
