package com.example.funfactsapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

object NotificationScheduler {

    fun scheduleDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // ✅ Fix applied here
        )

        val calendar = Calendar.getInstance().apply {
            add(Calendar.SECOND, 10) // ✅ Trigger notification after 10 seconds instead of waiting for 1 minute
        }


        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}
