package com.example.funfactsapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.funfactsapp.data.db.AppDatabase
import com.example.funfactsapp.data.repository.FactRepository
import com.example.funfactsapp.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackgroundFactFetchWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val factDao = AppDatabase.getDatabase(context).factDao()
                val repository = FactRepository(factDao)

                // Fetch a new fact and store it in the database
                val fact = repository.fetchRandomFact()
                fact?.let {
                    Log.d("BackgroundFactFetchWorker", "Fetched and saved fact: ${it.text}")

                    // ✅ Send a notification with the newly fetched fact
                    NotificationHelper.showNotification(context, it.text)
                    Log.d("BackgroundFactFetchWorker", "Notification sent: ${it.text}")
                } ?: Log.e("BackgroundFactFetchWorker", "Failed to fetch fact!")

                Result.success() // ✅ Work completed successfully
            } catch (e: Exception) {
                Log.e("BackgroundFactFetchWorker", "Error fetching fact", e)
                Result.retry() // 🔄 Retry if failed
            }
        }
    }
}
