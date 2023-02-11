package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.Subject
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface AcademicPerformanceApiService{
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("student/disciplines")
    suspend fun getAcademicPerformance(@Header ("Authorization") token: String ): List<Subject>
}