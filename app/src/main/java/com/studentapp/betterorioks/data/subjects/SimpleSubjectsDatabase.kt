package com.studentapp.betterorioks.data.subjects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.studentapp.betterorioks.model.subjectsFromSite.SimpleSubject

@Database(entities = [SimpleSubject::class], version = 1, exportSchema = false)
abstract class SimpleSubjectsDatabase: RoomDatabase() {
    abstract fun itemDao(): SimpleSubjectsDao
    companion object{
        @Volatile
        private var Instance: SimpleSubjectsDatabase? = null
        fun getDatabase(context: Context): SimpleSubjectsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SimpleSubjectsDatabase::class.java, "subjects_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}