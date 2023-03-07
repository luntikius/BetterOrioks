package com.studentapp.betterorioks.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface OrioksSubjectsApiService {
    @GET("student/student")

    suspend fun getSubjects(@Header("cookie")cookies: String):Response<ResponseBody>
}