package com.studentapp.betterorioks.data
import android.content.Context
import com.studentapp.betterorioks.data.schedule.ScheduleDatabase
import com.studentapp.betterorioks.data.schedule.ScheduleOfflineRepository


interface AppContainer {
    //val academicPerformanceRepository: AcademicPerformanceRepository
    val scheduleRepository: ScheduleOfflineRepository
    val orioksRepository: NetworkOrioksRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {

    override val scheduleRepository: ScheduleOfflineRepository by lazy {
        ScheduleOfflineRepository(ScheduleDatabase.getDatabase(context = context).itemDao())
    }

    override val orioksRepository: NetworkOrioksRepository by lazy{
        NetworkOrioksRepository()
    }
}
