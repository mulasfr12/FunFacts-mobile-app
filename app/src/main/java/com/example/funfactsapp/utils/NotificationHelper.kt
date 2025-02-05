package com.example.funfactsapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.funfactsapp.MainActivity
import com.example.funfactsapp.R

object NotificationHelper {

    private const val CHANNEL_ID = "fun_fact_channel"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Fun Facts Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for daily fun facts"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, factText: String) {
        createNotificationChannel(context) // ✅ Ensure channel is created

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_facts) // ✅ Ensure icon exists
            .setContentTitle("Daily Fun Fact")
            .setContentText(factText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(factText)) // ✅ Handle long text
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // ✅ Ensure high priority
            .build()

        val manager = NotificationManagerCompat.from(context)

        // ✅ Check for notification permission before calling manager.notify()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                manager.notify(1001, notification) // ✅ Send notification only if permission is granted
                Log.d("NotificationHelper", "Notification sent: $factText")
            } else {
                Log.e("NotificationHelper", "Permission denied: Cannot send notification.")
            }
        } else {
            manager.notify(1001, notification) // ✅ No permission needed below Android 13
            Log.d("NotificationHelper", "Notification sent: $factText")
        }
    }
}
