package com.example.funfactsapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.funfactsapp.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val factDao = AppDatabase.getDatabase(context).factDao()
            val facts = factDao.getAllFacts().firstOrNull()  // ✅ This is a List<Fact>

            facts?.randomOrNull()?.text?.let { factText ->  // ✅ Picks a random fact
                NotificationHelper.showNotification(context, factText)
            }
        }
    }
}

