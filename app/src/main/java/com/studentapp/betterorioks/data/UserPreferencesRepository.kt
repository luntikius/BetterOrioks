package com.studentapp.betterorioks.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.studentapp.betterorioks.model.ImportantDates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val TOKEN = stringPreferencesKey("token")
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
            preferences[SEMESTER_START] = dates.semester_start
            preferences[SESSION_START] = dates.session_start
        }
    }

    val token: Flow<String> = dataStore.data.catch{}.map{ preferences ->
        preferences[TOKEN] ?:""
    }

    val semesterStart: Flow<String> = dataStore.data.catch {}.map {preferences -> preferences[SEMESTER_START] ?: ""}

    val sessionStart: Flow<String> = dataStore.data.catch {}.map { preferences -> preferences[SESSION_START] ?: "" }

}