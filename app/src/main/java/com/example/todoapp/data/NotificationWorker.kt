package com.example.todoapp.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.R

class NotificationWorker(private val context: Context, workParams: WorkerParameters) :
    Worker(context, workParams) {

    override fun doWork(): Result {
        Log.i("asd", "doWork: Work request triggered")
        showNotification(
            context,
            inputData.getString("title").toString(),
            inputData.getString("message").toString()
        )
        return Result.success()
    }


    private fun showNotification(context: Context, title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "ChannelId"
            val channelName = R.string.notification_channel_name
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(channelId, channelName.toString(), importance)

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
        val notificationId = "NotificationId"
        val notification =
            NotificationCompat.Builder(context, notificationId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build()

        val notificationManager =
            NotificationManagerCompat.from(context.applicationContext)
        notificationManager.notify(0, notification)
    }
}
