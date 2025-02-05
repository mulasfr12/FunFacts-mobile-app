package com.example.funfactsapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.util.Log
import com.example.funfactsapp.data.db.AppDatabase
import com.example.funfactsapp.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class FactNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val factDao = AppDatabase.getDatabase(context).factDao()
                val factsList = factDao.getAllFacts().firstOrNull() // ✅ This returns List<Fact> or null

                val fact = factsList?.randomOrNull() // ✅ Pick one random fact safely

                if (fact != null) {
                    NotificationHelper.showNotification(context, fact.text)  // ✅ Use fact.text safely
                    Log.d("FactNotificationWorker", "Notification sent: ${fact.text}")
                } else {
                    Log.e("FactNotificationWorker", "No facts found to show in notification!")
                }

                Result.success()  // ✅ Work completed successfully
            } catch (e: Exception) {
                Log.e("FactNotificationWorker", "Error showing notification", e)
                Result.retry()  // 🔄 Retry if failed
            }
        }
    }
}
