package com.example.l3z1_4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val image = intent?.extras?.getInt("image")
            val title = intent?.extras?.getString("title")
            val id = intent?.extras?.getInt("id")
            val builder = NotificationCompat.Builder(context, "notifyTask")
                    .setSmallIcon(image ?: R.drawable.general_icon)
                    .setContentTitle(title ?: "Zadanie")
                    .setContentText("Za 15 minut masz zadanie")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(id ?: (1..1000).random(), builder.build())
        }
    }
}