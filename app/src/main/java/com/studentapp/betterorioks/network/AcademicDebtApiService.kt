package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.AcademicDebt
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface AcademicDebtApiService{
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("student/academic-debts")
    suspend fun getAcademicDebt(@Header("Authorization") token: String ): List<AcademicDebt>
}