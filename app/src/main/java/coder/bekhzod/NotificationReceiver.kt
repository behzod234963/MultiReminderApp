package coder.bekhzod

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var alarmManager: AlarmManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("TAG", "onCreate: NotificationReceiver is started")

        val notificationScheduler = NotificationScheduler(notificationManager, alarmManager)
        if (context != null) {
            notificationScheduler.scheduleNotification(context, 0)
        }
        if (context != null) {
            notificationScheduler.showNotification(ctx = context, title = "Hello world","My name is Bekhzod")
        }
    }
}