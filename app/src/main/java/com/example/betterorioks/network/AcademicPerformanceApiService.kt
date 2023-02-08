package com.example.betterorioks.network

import com.example.betterorioks.data.AppDetails
import com.example.betterorioks.model.Subject
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