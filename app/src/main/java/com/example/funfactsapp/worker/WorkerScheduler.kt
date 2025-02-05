package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerScheduler {

    fun scheduleBackgroundFactFetchWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<BackgroundFactFetchWorker>(
            1, TimeUnit.MINUTES // ✅ Runs every 1 minute for testing (change to 12 hours for production)
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // ✅ Only fetch if online
                .setRequiresBatteryNotLow(true) // ✅ Runs only when battery is not low
                .build()
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "BackgroundFactFetchWorker",
            ExistingPeriodicWorkPolicy.KEEP, // ✅ Avoids duplicate workers
            workRequest
        )
    }
}
