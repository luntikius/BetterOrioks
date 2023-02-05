package com.example.betterorioks.network

import com.example.betterorioks.model.SubjectMore
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface AcademicPerformanceMoreApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: BetterOrioks/0.2 Android 13"
    )
    @GET("student/disciplines/{discipline_id}/events")
    suspend fun getAcademicPerformanceMore(@Header("Authorization") token: String ,@Path("discipline_id") disciplineId: String): List<SubjectMore>
}