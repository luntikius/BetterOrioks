package com.studentapp.betterorioks.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface OrioksAuthInfoApiService {
    @GET("user/login")

    suspend fun getAuthInfo(): Response<ResponseBody>
}