package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerScheduler {

    fun scheduleBackgroundFactFetchWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<BackgroundFactFetchWorker>(
            12, TimeUnit.HOURS // ✅ Runs every 12 hours
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "BackgroundFactFetchWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun scheduleFactNotificationWorker(context: Context) {
        val notificationWorkRequest = PeriodicWorkRequestBuilder<FactNotificationWorker>(
            12, TimeUnit.HOURS // ✅ Runs every 12 hours
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "FactNotificationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }

    fun cancelAllWorkers(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("BackgroundFactFetchWorker")
        WorkManager.getInstance(context).cancelUniqueWork("FactNotificationWorker")
    }
}
