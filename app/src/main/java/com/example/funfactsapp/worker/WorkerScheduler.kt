package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerScheduler {

    fun scheduleFactFetchWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<FactFetchWorker>(
            1, TimeUnit.DAYS // Run every 24 hours
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Ensure internet is available
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "FactFetchWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}
