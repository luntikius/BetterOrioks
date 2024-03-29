package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.ImportantDates
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ImportantDatesApiService{
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("schedule")
    suspend fun getImportantDates(@Header("Authorization") token: String ): ImportantDates
}