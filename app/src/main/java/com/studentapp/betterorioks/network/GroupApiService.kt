package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.schedule.GroupElement
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface GroupApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("schedule/groups")
    suspend fun getGroup(@Header("Authorization") token: String ): List<GroupElement>
}