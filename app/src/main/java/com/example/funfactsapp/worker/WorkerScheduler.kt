package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerScheduler {

    fun scheduleFactFetchWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<FactFetchWorker>(24, TimeUnit.HOURS) // Fetch facts every 24 hours
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Only fetch if online
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "FactFetchWorker",
            ExistingPeriodicWorkPolicy.KEEP, // Avoid duplicate workers
            workRequest
        )
    }

    fun scheduleFactNotificationWorker(context: Context) {
        val notificationWorkRequest = OneTimeWorkRequestBuilder<FactNotificationWorker>() // ✅ Runs once immediately
            .setInitialDelay(5, TimeUnit.SECONDS) // ✅ Runs after 5 seconds (for testing)
            .build()

        WorkManager.getInstance(context).enqueue(notificationWorkRequest)
    }

}
