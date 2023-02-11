package com.studentapp.betterorioks.data

import com.studentapp.betterorioks.model.Token
import com.studentapp.betterorioks.network.TokenApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

class NetworkTokenRepository
    (
    private val loginDetails: String = ""
)
{
    private val BASE_URL =
        "https://orioks.miet.ru/api/v1/"


    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .baseUrl(BASE_URL)
        .build()

    private val tokenRetrofitService: TokenApiService by lazy {
        retrofit.create(TokenApiService::class.java)
    }

    suspend fun getToken(): Token = tokenRetrofitService.getToken(loginDetails = "Basic $loginDetails")
}