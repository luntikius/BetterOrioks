package com.example.betterorioks.network

import com.example.betterorioks.data.AppDetails
import com.example.betterorioks.model.TimeTableElement
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TimeTableApiService{
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("schedule/timetable")
    suspend fun getTimeTable(@Header("Authorization") token: String ): TimeTableElement
}