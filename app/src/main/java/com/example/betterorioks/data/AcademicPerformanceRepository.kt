package com.example.betterorioks.data

import com.example.betterorioks.model.Subject
import com.example.betterorioks.network.AcademicPerformanceApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

class NetworkAcademicPerformanceRepository(private val token:String)
{
    private val BASE_URL =
        "https://orioks.miet.ru/api/v1/"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .baseUrl(BASE_URL)
        .build()

    //RetrofitServices
    private val academicPerformanceRetrofitService: AcademicPerformanceApiService by lazy {
        retrofit.create(AcademicPerformanceApiService::class.java)
    }

    suspend fun getAcademicPerformance():List<Subject> {
        return academicPerformanceRetrofitService.getAcademicPerformance(token = "Bearer $token")
    }
}