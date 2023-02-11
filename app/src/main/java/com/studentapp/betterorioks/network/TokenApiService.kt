package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.Token
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TokenApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @GET("auth")
    suspend fun getToken(@Header("Authorization") loginDetails: String): Token
}