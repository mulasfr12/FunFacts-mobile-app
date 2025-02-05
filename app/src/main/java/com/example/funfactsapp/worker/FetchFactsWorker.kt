package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.util.Log
import com.example.funfactsapp.data.db.AppDatabase
import com.example.funfactsapp.data.repository.FactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FactFetchWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val factDao = AppDatabase.getDatabase(context).factDao()
                val repository = FactRepository(factDao)

                // Fetch 1 random fact
                val fact = repository.fetchRandomFact()
                fact?.let {
                    Log.d("FactFetchWorker", "Fetched fact: ${it.text}")
                } ?: Log.e("FactFetchWorker", "Failed to fetch fact!")

                Result.success() // ✅ Work completed successfully
            } catch (e: Exception) {
                Log.e("FactFetchWorker", "Error fetching fact", e)
                Result.retry() // 🔄 Retry if failed
            }
        }
    }
}
