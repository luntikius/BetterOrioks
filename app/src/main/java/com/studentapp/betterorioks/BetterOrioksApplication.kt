package com.studentapp.betterorioks

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.studentapp.betterorioks.data.AppContainer
import com.studentapp.betterorioks.data.DefaultAppContainer
import com.studentapp.betterorioks.data.NetworkScheduleFromSiteRepository
import com.studentapp.betterorioks.data.UserPreferencesRepository

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)
class BetterOrioksApplication:Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var container: AppContainer
    lateinit var networkScheduleFromSiteRepository: NetworkScheduleFromSiteRepository
    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        networkScheduleFromSiteRepository = NetworkScheduleFromSiteRepository()
        container = DefaultAppContainer()
    }
}