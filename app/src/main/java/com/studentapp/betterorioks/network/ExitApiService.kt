package com.studentapp.betterorioks.network

import com.studentapp.betterorioks.data.AppDetails
import com.studentapp.betterorioks.model.Exit
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface ExitApiService {
    @Headers(
        "Accept: application/json",
        "User-Agent: ${AppDetails.appName}/${AppDetails.version} Android"
    )
    @DELETE("student/tokens/{token}")
    suspend fun getToken(@Header("Authorization") bearerToken: String, @Path("token") token: String): Exit
}