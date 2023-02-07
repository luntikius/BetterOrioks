package com.example.betterorioks.network

import com.example.betterorioks.model.Exit
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface ExitApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: BetterOrioks/0.2 Android 13"
    )
    @DELETE("student/tokens/{token}")
    suspend fun getToken(@Header("Authorization") bearerToken: String, @Path("token") token: String): Exit
}