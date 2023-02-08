package com.example.betterorioks.network

import com.example.betterorioks.data.AppDetails
import com.example.betterorioks.model.UserInfo
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