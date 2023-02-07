package com.example.betterorioks.data

import com.example.betterorioks.model.Exit
import com.example.betterorioks.network.ExitApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

class NetworkExitRepository
    (
    private val token: String = ""
)
{
    private val BASE_URL =
        "https://orioks.miet.ru/api/v1/"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .baseUrl(BASE_URL)
        .build()

    private val exitRetrofitService: ExitApiService by lazy {
        retrofit.create(ExitApiService::class.java)
    }

    suspend fun removeToken(): Exit = exitRetrofitService.getToken("Bearer $token",token)
}