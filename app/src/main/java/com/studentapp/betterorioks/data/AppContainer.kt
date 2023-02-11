package com.studentapp.betterorioks.data

interface AppContainer {
    //val academicPerformanceRepository: AcademicPerformanceRepository
}

class DefaultAppContainer(): AppContainer {

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
}
