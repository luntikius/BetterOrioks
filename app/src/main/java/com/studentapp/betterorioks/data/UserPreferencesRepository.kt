package com.studentapp.betterorioks.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun setToken(token: String) {
        dataStore.edit{preferences ->
            preferences[TOKEN] = token
        }
    }

    val token: Flow<String> = dataStore.data.catch{}.map{ preferences ->
        preferences[TOKEN] ?:""
    }

}