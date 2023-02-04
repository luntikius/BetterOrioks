package com.example.betterorioks.data

import com.example.betterorioks.network.AcademicPerformanceApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

interface AppContainer {
    val academicPerformanceRepository: AcademicPerformanceRepository
}

class DefaultAppContainer(): AppContainer{
    private val BASE_URL =
        "https://orioks.miet.ru/api/v1/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: AcademicPerformanceApiService by lazy {
        retrofit.create(AcademicPerformanceApiService::class.java)
    }

    override val academicPerformanceRepository: AcademicPerformanceRepository by lazy{
        NetworkAcademicPerformanceRepository(retrofitService, "Bearer " + Temp.token)
    }
}
