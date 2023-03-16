package com.studentapp.betterorioks.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface ResourcesApiService {
    @GET("student/ir/")
    suspend fun getResources(@Header("cookie")cookies: String, @QueryMap(encoded = true)query: Map<String,String>): ResponseBody
}