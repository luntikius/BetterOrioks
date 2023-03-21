package com.studentapp.betterorioks.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NewsApiService {
    @GET("{link}")
    suspend fun getNews(@Header("cookie")cookies: String, @Path("link") link: String = ""): ResponseBody
}