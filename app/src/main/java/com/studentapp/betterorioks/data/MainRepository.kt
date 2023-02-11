package com.studentapp.betterorioks.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.studentapp.betterorioks.model.*
import com.studentapp.betterorioks.model.schedule.GroupElement
import com.studentapp.betterorioks.model.schedule.TimeTableElement
import com.studentapp.betterorioks.network.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

class NetworkMainRepository(token:String)
{
    private val BASE_URL =
        "https://orioks.miet.ru/api/v1/"

    private val finalToken = "Bearer $token"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .baseUrl(BASE_URL)
        .build()

    //RetrofitServices
    private val academicPerformanceRetrofitService: AcademicPerformanceApiService by lazy {
        retrofit.create(AcademicPerformanceApiService::class.java)
    }

    private val userInfoRetrofitService: UserInfoApiService by lazy {
        retrofit.create(UserInfoApiService::class.java)
    }

    private val academicDebtRetrofitService: AcademicDebtApiService by lazy {
        retrofit.create(AcademicDebtApiService::class.java)
    }

    private val importantDatesRetrofitService: ImportantDatesApiService by lazy{
        retrofit.create(ImportantDatesApiService::class.java)
    }

    private val timeTableRetrofitService: TimeTableApiService by lazy{
        retrofit.create(TimeTableApiService::class.java)
    }

    private val groupRetrofitService: GroupApiService by lazy{
        retrofit.create(GroupApiService::class.java)
    }

    //get functions
    suspend fun getAcademicPerformance():List<Subject> {
        return academicPerformanceRetrofitService.getAcademicPerformance(token = finalToken)
    }

    suspend fun getUserInfo(): UserInfo {
        return userInfoRetrofitService.getUserInfo(token = finalToken)
    }

    suspend fun getAcademicDebt():List<AcademicDebt>{
        return academicDebtRetrofitService.getAcademicDebt(token = finalToken)
    }

    suspend fun getImportantDates(): ImportantDates {
        return importantDatesRetrofitService.getImportantDates(token = finalToken)
    }

    suspend fun getTimeTable(): TimeTableElement {
        return timeTableRetrofitService.getTimeTable(token = finalToken)
    }

    suspend fun getGroup(): List<GroupElement> {
        return groupRetrofitService.getGroup(token = finalToken)
    }
}
