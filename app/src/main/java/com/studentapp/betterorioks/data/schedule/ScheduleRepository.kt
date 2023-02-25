package com.studentapp.betterorioks.data.schedule

import com.studentapp.betterorioks.model.scheduleFromSite.FullSchedule
import com.studentapp.betterorioks.network.ScheduleApiService
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import retrofit2.Retrofit

class NetworkScheduleFromSiteRepository
{
    private val BASE_URL =
        "https://www.miet.ru"

    private val retrofitString = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()

    private val scheduleFromSiteRetrofitService: ScheduleApiService by lazy {
        retrofitString.create(ScheduleApiService::class.java)
    }
    suspend fun getSchedule(
        group: String,
        setSchedule: (fullSchedule: FullSchedule) -> Unit
    ){
        val response = scheduleFromSiteRetrofitService.getSchedule("", group = group).string()
        val start = response.indexOf('"')
        val end = response.indexOf(";")
        val cookie = response.slice(start+1 until end)
        val fullSchedule = Json.decodeFromString<FullSchedule>(
            string = scheduleFromSiteRetrofitService.getSchedule(cookie = cookie, group = group).string(),
        )
        setSchedule(fullSchedule)
    }

}