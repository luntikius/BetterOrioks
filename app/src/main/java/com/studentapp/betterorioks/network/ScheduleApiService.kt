package com.studentapp.betterorioks.network

import okhttp3.ResponseBody
import retrofit2.http.*

interface ScheduleApiService {
    @Headers(
        "accept: */*",
        "content-type: application/x-www-form-urlencoded; charset=UTF-8",
    )
    @FormUrlEncoded
    @POST("schedule/data")
    suspend fun getSchedule(@Header("cookie")cookie: String, @Field("group")group: String): ResponseBody
}