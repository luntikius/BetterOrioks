package com.studentapp.betterorioks.data.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.studentapp.betterorioks.model.scheduleFromSite.SimpleScheduleElement
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(schedule: SimpleScheduleElement)

    @Query("SELECT * from schedule WHERE day = :day")
    fun getSchedule(day:Int): Flow<List<SimpleScheduleElement>>

    @Query("DELETE FROM schedule")
    suspend fun dump()

    @Query("SELECT COUNT(*) FROM schedule")
    suspend fun count():Int
}