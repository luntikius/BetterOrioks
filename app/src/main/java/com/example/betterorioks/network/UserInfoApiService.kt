package com.example.betterorioks.network

import com.example.betterorioks.model.UserInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface UserInfoApiService{
    @Headers(
        "Accept: application/json",
        "User-Agent: BetterOrioks/0.2 Android 13"
    )
    @GET("student")
    suspend fun getUserInfo(@Header("Authorization") token: String ): UserInfo
}