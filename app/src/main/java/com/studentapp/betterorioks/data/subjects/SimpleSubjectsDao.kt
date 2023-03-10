package com.studentapp.betterorioks.data.subjects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.studentapp.betterorioks.model.subjectsFromSite.SimpleSubject
import kotlinx.coroutines.flow.Flow

@Dao
interface SimpleSubjectsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(controlEvent: SimpleSubject)

    @Query("SELECT * from subjects")
    fun getAll(): Flow<List<SimpleSubject>>

    @Query("UPDATE subjects SET userScore = 2 WHERE id < 3")
    suspend fun modify()

    @Query("SELECT * FROM subjects WHERE isSubject = true")
    fun getSubjects(): Flow<List<SimpleSubject>>

    @Query("SELECT * FROM subjects WHERE systemId = :systemId AND isSubject = false")
    fun getControlEvents(systemId: Int): Flow<List<SimpleSubject>>

    @Query("DELETE FROM subjects")
    suspend fun dump()
}