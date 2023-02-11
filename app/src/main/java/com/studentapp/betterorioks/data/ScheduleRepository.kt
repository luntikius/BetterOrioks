package com.studentapp.betterorioks.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.studentapp.betterorioks.model.schedule.Schedule
import com.studentapp.betterorioks.network.ScheduleApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

class NetworkScheduleRepository
    (
    private val id: Int = 0,
    private val token: String
)
{
    private val BASE_URL =
        "https://orioks.miet.ru/api/v1/"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .baseUrl(BASE_URL)
        .build()

    private val scheduleRetrofitService: ScheduleApiService by lazy {
        retrofit.create(ScheduleApiService::class.java)
    }

    suspend fun getSchedule():List<Schedule> =
        scheduleRetrofitService.getSchedule(
            token = "Bearer $token",
            id = id.toString()
        )
}