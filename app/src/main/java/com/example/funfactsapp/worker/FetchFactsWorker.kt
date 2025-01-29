package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.funfactsapp.data.repository.FactRepository
import com.example.funfactsapp.utils.NotificationHelper
import kotlinx.coroutines.runBlocking

class FactFetchWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: FactRepository
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            // Perform the background task using coroutines
            runBlocking {
                val randomFact = repository.fetchRandomFact()
                if (randomFact != null) {
                    // Trigger a notification with the random fact
                    NotificationHelper.showFactNotification(
                        context = applicationContext,
                        title = "Fun Fact",
                        message = randomFact.text
                    )
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
