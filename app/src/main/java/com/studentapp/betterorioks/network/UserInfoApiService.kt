package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.UserInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface UserInfoApiService{
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("student")
    suspend fun getUserInfo(@Header("Authorization") token: String ): UserInfo
}