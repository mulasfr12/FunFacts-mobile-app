package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerScheduler {

    fun scheduleFactFetchWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<FactFetchWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Ensure internet is available
                    .setRequiresBatteryNotLow(true) // Prevents running on low battery
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "FactFetchWorker",
            ExistingPeriodicWorkPolicy.KEEP, // Keeps existing worker to avoid duplication
            workRequest
        )
    }
}
