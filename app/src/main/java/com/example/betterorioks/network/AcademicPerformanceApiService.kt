package com.example.betterorioks.network

import com.example.betterorioks.model.Subject
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface AcademicPerformanceApiService{
    @Headers(
        "Accept: application/json",
        "User-Agent: BetterOrioks/0.2 Android 13"
    )
    @GET("student/disciplines")
    suspend fun getAcademicPerformance(@Header ("Authorization") token: String ): List<Subject>
}