package com.studentapp.betterorioks.data.schedule

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.studentapp.betterorioks.model.scheduleFromSite.SimpleScheduleElement

@Database(entities = [SimpleScheduleElement::class], version = 2, exportSchema = false)
abstract class ScheduleDatabase: RoomDatabase() {
    abstract fun itemDao(): ScheduleDao
    companion object{
        @Volatile
        private var Instance: ScheduleDatabase? = null
        fun getDatabase(context: Context): ScheduleDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ScheduleDatabase::class.java, "schedule_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}