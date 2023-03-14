package com.studentapp.betterorioks.data.subjects

import android.util.Log
import com.studentapp.betterorioks.model.subjectsFromSite.SimpleSubject
import kotlinx.coroutines.flow.Flow

class SimpleSubjectsOfflineRepository(private val subjectsDao: SimpleSubjectsDao) {

    suspend fun modify() = subjectsDao.modify()

    fun getSubjects(): Flow<List<SimpleSubject>> = subjectsDao.getSubjects()

    fun getControlEvents(systemId: Int): Flow<List<SimpleSubject>> = subjectsDao.getControlEvents(systemId = systemId)

    suspend fun insertItem(item: SimpleSubject) = subjectsDao.insert(item)

    suspend fun insertItems(items: List<SimpleSubject>){
        dump()
        items.forEach { subjectsDao.insert(it) }
    }
    suspend fun dump() {
        Log.d("SubjectsOfflineRepository","dumped")
        subjectsDao.dump()
    }

}