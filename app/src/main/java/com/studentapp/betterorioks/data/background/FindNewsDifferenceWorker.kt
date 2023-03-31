package com.studentapp.betterorioks.data.background

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.studentapp.betterorioks.data.NetworkOrioksRepository
import com.studentapp.betterorioks.data.UserPreferencesRepository
import com.studentapp.betterorioks.ui.components.makeStatusNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class FindNewsDifferenceWorker (
    context: Context,
    params: WorkerParameters,
    ): CoroutineWorker(appContext = context, params = params){

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "layout_preferences")

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {

                val cookies = inputData.getString("COOKIES")

                require(!cookies.isNullOrBlank()){
                    val errorMessage = "invalid cookies"
                    Log.e("FIND_NEWS_DIFFERENCE_WORKER", "invalid cookies")
                    errorMessage
                }
                Log.d("FIND_NEWS_DIFFERENCE_WORKER","run")
                val orioksRepository = NetworkOrioksRepository()
                val userPreferencesRepository = UserPreferencesRepository(applicationContext.dataStore)
                val newData = orioksRepository.getNews(cookies = cookies)[0]
                val oldData = userPreferencesRepository.lastNewsLink.first()
                if(newData.link != oldData){
                    userPreferencesRepository.setLastNewsLink(newData.link)
                    makeStatusNotification(
                        head = "Обновление новостей",
                        message = newData.name,
                        context = applicationContext,
                        link = newData.link
                    )
                }
                Result.success()
            } catch (throwable: Throwable) {
                Log.e(
                    "FIND_DIFFERENCE_WORKER",
                    "error finding differences",
                    throwable
                )
                Result.failure()
            }
        }
    }

}
