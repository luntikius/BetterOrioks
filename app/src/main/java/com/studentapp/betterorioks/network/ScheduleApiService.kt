package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.schedule.Schedule
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface ScheduleApiService
{
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("schedule/groups/{id}")
    suspend fun getSchedule(@Header("Authorization") token: String, @Path("id")id: String ): List<Schedule>
}