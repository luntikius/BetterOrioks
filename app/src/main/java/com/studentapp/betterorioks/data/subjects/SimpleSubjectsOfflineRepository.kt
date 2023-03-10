package com.studentapp.betterorioks.data.subjects

import com.studentapp.betterorioks.model.subjectsFromSite.SimpleSubject
import kotlinx.coroutines.flow.Flow

class SimpleSubjectsOfflineRepository(private val subjectsDao: SimpleSubjectsDao) {

    suspend fun modify() = subjectsDao.modify()

    fun getSubjects(): Flow<List<SimpleSubject>> = subjectsDao.getSubjects()

    fun getControlEvents(systemId: Int): Flow<List<SimpleSubject>> = subjectsDao.getControlEvents(systemId = systemId)

    suspend fun insertItem(item: SimpleSubject) = subjectsDao.insert(item)

    suspend fun dump() = subjectsDao.dump()

}