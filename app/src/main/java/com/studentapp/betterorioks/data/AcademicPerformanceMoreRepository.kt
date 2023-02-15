package com.studentapp.betterorioks.data


import com.studentapp.betterorioks.model.SubjectMore
import com.studentapp.betterorioks.network.AcademicPerformanceMoreApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit


class NetworkAcademicPerformanceMoreRepository
    (
        private val disciplineId: Int = 0,
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

    private val academicPerformanceMoreRetrofitService: AcademicPerformanceMoreApiService by lazy {
        retrofit.create(AcademicPerformanceMoreApiService::class.java)
    }

    suspend fun getAcademicPerformanceMore():List<SubjectMore> =
        academicPerformanceMoreRetrofitService.getAcademicPerformanceMore(
            token = "Bearer $token",
            disciplineId = disciplineId.toString()
        )
}