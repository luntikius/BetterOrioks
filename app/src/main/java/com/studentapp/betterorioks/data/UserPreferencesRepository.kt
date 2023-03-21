package com.studentapp.betterorioks.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
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
        val AUTH_COOKIES = stringPreferencesKey("authCookies")
        val CSRF = stringPreferencesKey("csrf")
        val LOGIN = stringPreferencesKey("login")
        val PASSWORD = stringPreferencesKey("password")
        val STUDENT_ID = intPreferencesKey("studentId")
        val FULL_NAME = stringPreferencesKey("fullName")
        val GROUP = stringPreferencesKey("group")
        val STUDY_DIRECTION = stringPreferencesKey("studyDirection")
        val DEPARTMENT = stringPreferencesKey("department")
        val SEMESTER_START = stringPreferencesKey("sessionEnd")
        val SESSION_START = stringPreferencesKey("sessionStart")
        val SEND_NOTIFICATIONS = booleanPreferencesKey("sendNotifications")
        val LAST_NEWS_LINK = stringPreferencesKey("lastNewsLink")
    }

    suspend fun setToken(token: String) {
        dataStore.edit{preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun setCookies(cookies: String){
        dataStore.edit { preferences ->
            preferences[AUTH_COOKIES] = cookies
        }
    }

    suspend fun setCsrf(csrf: String){
        dataStore.edit { preferences ->
            preferences[CSRF] = csrf
        }
    }

    suspend fun setLoginAndPassword(login: String, password: String){
        dataStore.edit { preferences ->
            preferences[LOGIN] = login
            preferences[PASSWORD] = password
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

    suspend fun setSendNotifications(value: Boolean){
        dataStore.edit{
            preferences ->
            preferences[SEND_NOTIFICATIONS] = value
        }
    }

    suspend fun dump(){
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[AUTH_COOKIES] = ""
            preferences[LOGIN] = ""
            preferences[PASSWORD] = ""
            preferences[SEND_NOTIFICATIONS] = false
            preferences[SESSION_START] = ""
            preferences[SEMESTER_START] = ""
            preferences[STUDENT_ID] = 0
            preferences[FULL_NAME] = ""
            preferences[GROUP] = ""
            preferences[STUDY_DIRECTION] = ""
            preferences[DEPARTMENT] = ""
            preferences[LAST_NEWS_LINK] = ""
        }
    }

    suspend fun setLastNewsLink(link: String){
        dataStore.edit {
            preferences ->
            preferences[LAST_NEWS_LINK] = link
        }
    }

    val token: Flow<String> = dataStore.data.catch{}.map{ preferences ->
        preferences[TOKEN] ?: ""
    }
    val authCookies: Flow<String> = dataStore.data.catch {}.map { preferences ->
        preferences[AUTH_COOKIES] ?: ""
    }
    val semesterStart: Flow<String> = dataStore.data.catch {}.map {preferences -> preferences[SEMESTER_START] ?: ""}
    val sessionStart: Flow<String> = dataStore.data.catch {}.map { preferences -> preferences[SESSION_START] ?: "" }
    val studentId: Flow<Int> = dataStore.data.catch {  }.map { preferences -> preferences[STUDENT_ID] ?: 0 }
    val fullName: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[FULL_NAME] ?: "" }
    val group: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[GROUP] ?: "" }
    val studyDirection: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[STUDY_DIRECTION] ?: "" }
    val department: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[DEPARTMENT] ?: "" }
    val login: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[LOGIN] ?: "" }
    val password: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[PASSWORD] ?: "" }
    val csrf: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[CSRF] ?: "" }
    val lastNewsLink: Flow<String> = dataStore.data.catch {  }.map { preferences -> preferences[LAST_NEWS_LINK] ?: "" }
    val sendNotifications: Flow<Boolean> = dataStore.data.catch {  }.map { preferences -> preferences[SEND_NOTIFICATIONS] ?: false }
}