package com.studentapp.betterorioks.data
import android.content.Context
import com.studentapp.betterorioks.data.schedule.ScheduleDatabase
import com.studentapp.betterorioks.data.schedule.ScheduleOfflineRepository


interface AppContainer {
    //val academicPerformanceRepository: AcademicPerformanceRepository
    val scheduleRepository: ScheduleOfflineRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {

//    private val BASE_URL =
//        "https://orioks.miet.ru/api/v1/"
//
//    @OptIn(ExperimentalSerializationApi::class)
//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
//        .baseUrl(BASE_URL)
//        .build()
//
//    //RetrofitServices
//    private val academicPerformanceRetrofitService: AcademicPerformanceApiService by lazy {
//        retrofit.create(AcademicPerformanceApiService::class.java)
//    }
//
//    //Repository calls
//    override val academicPerformanceRepository: AcademicPerformanceRepository by lazy{
//        NetworkAcademicPerformanceRepository(academicPerformanceRetrofitService, token = "Bearer " + token)
//    }
    override val scheduleRepository: ScheduleOfflineRepository by lazy {
        ScheduleOfflineRepository(ScheduleDatabase.getDatabase(context = context).itemDao())
    }
}
