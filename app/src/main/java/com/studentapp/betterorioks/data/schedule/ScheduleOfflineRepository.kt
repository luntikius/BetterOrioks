package com.studentapp.betterorioks.data.schedule

import com.studentapp.betterorioks.model.scheduleFromSite.SimpleScheduleElement
import kotlinx.coroutines.flow.Flow

class ScheduleOfflineRepository(private val scheduleDao: ScheduleDao) {
    fun getAllItemsStream(day:Int): Flow<List<SimpleScheduleElement>> = scheduleDao.getSchedule(day)

    suspend fun insertItem(item: SimpleScheduleElement) = scheduleDao.insert(item)

    suspend fun dump() = scheduleDao.dump()

    suspend fun count() = scheduleDao.count()
}