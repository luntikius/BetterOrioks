package com.studentapp.betterorioks.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.studentapp.betterorioks.model.ImportantDates
import com.studentapp.betterorioks.model.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val TOKEN = stringPreferencesKey("token")
        val STUDENT_ID = intPreferencesKey("studentId")
        val FULL_NAME = stringPreferencesKey("fullName")
        val GROUP = stringPreferencesKey("group")
        val STUDY_DIRECTION = stringPreferencesKey("studyDirection")
        val DEPARTMENT = stringPreferencesKey("department")
        val SEMESTER_START = stringPreferencesKey("sessionEnd")
        val SESSION_START = stringPreferencesKey("sessionStart")
    }

    suspend fun setToken(token: String) {
        dataStore.edit{preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun setImportantDates(dates: ImportantDates){
        dataStore.edit {preferences ->
            preferences[SEMESTER_START] = dates.semesterStart
            preferences[SESSION_START] = dates.sessionStart
        }
    }

    suspend fun setUserInfo(userInfo: UserInfo){
        dataStore.edit {preferences ->
            preferences[STUDENT_ID] = userInfo.recordBookId
            preferences[FULL_NAME] = userInfo.fullName
            preferences[GROUP] = userInfo.group
            preferences[STUDY_DIRECTION] = userInfo.studyDirection
            preferences[DEPARTMENT] = userInfo.department
        }
    }

    suspend fun dump(){
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[SESSION_START] = ""
            preferences[SEMESTER_START] = ""
            preferences[STUDENT_ID] = 0
            preferences[FULL_NAME] = ""
            preferences[GROUP] = ""
            preferences[STUDY_DIRECTION] = ""
            preferences[DEPARTMENT] = ""
        }
    }

    val token: Flow<String> = dataStore.data.catch{}.map{ preferences ->
        preferences[TOKEN] ?:""
    }
    val semesterStart: Flow<String> = dataStore.data.catch {}.map {preferences -> preferences[SEMESTER_START] ?: ""}
    val sessionStart: Flow<String> = dataStore.data.catch {}.map { preferences -> preferences[SESSION_START] ?: "" }
    val studentId: Flow<Int> = dataStore.data.catch {  }.map { preferences -> preferences[STUDENT_ID] ?: 0 }
    val fullName: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[FULL_NAME] ?: "" }
    val group: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[GROUP] ?: "" }
    val studyDirection: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[STUDY_DIRECTION] ?: "" }
    val department: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[DEPARTMENT] ?: "" }

}