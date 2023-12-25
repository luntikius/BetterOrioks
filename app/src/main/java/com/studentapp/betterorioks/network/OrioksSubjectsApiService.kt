package com.studentapp.betterorioks.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface OrioksSubjectsApiService {
    @GET("student/student")

    suspend fun getSubjects(@Header("cookie")cookies: String, @QueryMap(encoded = true)query: Map<String,String> = mapOf()):Response<ResponseBody>
}