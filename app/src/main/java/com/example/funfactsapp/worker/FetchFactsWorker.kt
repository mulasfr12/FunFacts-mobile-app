package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.funfactsapp.data.repository.FactRepository
import com.example.funfactsapp.utils.NotificationHelper
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class FactFetchWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val repository = FactRepositorySingleton.repository // Get repository instance
            val randomFact = repository.fetchRandomFact()

            randomFact?.let {
                // Trigger a notification with the random fact
                NotificationHelper.showFactNotification(
                    context = applicationContext,
                    title = "Fun Fact",
                    message = it.text
                )
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
