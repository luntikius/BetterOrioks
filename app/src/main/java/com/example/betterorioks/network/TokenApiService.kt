package com.example.betterorioks.network

import com.example.betterorioks.model.Token
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TokenApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: BetterOrioks/0.2 Android 13"
    )
    @GET("auth")
    suspend fun getToken(@Header("Authorization") loginDetails: String): Token
}