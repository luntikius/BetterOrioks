package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.SubjectMore
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface AcademicPerformanceMoreApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("student/disciplines/{discipline_id}/events")
    suspend fun getAcademicPerformanceMore(@Header("Authorization") token: String ,@Path("discipline_id") disciplineId: String): List<SubjectMore>
}