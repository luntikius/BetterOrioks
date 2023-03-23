package com.studentapp.betterorioks.data.background

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkManagerBetterOrioksRepository(context: Context) {

    //val outputWorkInfo: Flow<WorkInfo?> = MutableStateFlow(null)

    private val workManager = WorkManager.getInstance(context)

    fun checkForUpdates(
        cookies: String
    ){
        val cookiesData = createInputCookies(cookies = cookies)
        val differenceBuilder =
            PeriodicWorkRequestBuilder<FindDifferencesWorker>(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,TimeUnit.MILLISECONDS)
            .setInputData(cookiesData)
            .setConstraints(constraints = Constraints(NetworkType.CONNECTED))
            .addTag("CheckForAcademicPerformance")
            .build()
        workManager.enqueueUniquePeriodicWork(
            "CheckForAcademicPerformance",
            ExistingPeriodicWorkPolicy.UPDATE,
            differenceBuilder
        )
    }

    fun CheckForNews(
        cookies: String
    ){
        val cookiesData = createInputCookies(cookies = cookies)
        val differenceBuilder =
            PeriodicWorkRequestBuilder<FindNewsDifferenceWorker>(2,TimeUnit.HOURS)
                .setInputData(cookiesData)
                .setConstraints(constraints = Constraints(NetworkType.CONNECTED))
                .addTag("CheckForNews")
                .build()
        workManager.enqueueUniquePeriodicWork(
            "CheckForNews",
            ExistingPeriodicWorkPolicy.UPDATE,
            differenceBuilder
        )
    }

    fun cancelChecks() {
        workManager.cancelUniqueWork("CheckForAcademicPerformance")
    }

    fun cancelNewsChecks(){
        workManager.cancelUniqueWork("CheckForNews")
    }


    private fun createInputCookies(cookies:String): Data {
        val builder = Data.Builder()
        builder.putString("COOKIES", cookies)
        return builder.build()
    }
}