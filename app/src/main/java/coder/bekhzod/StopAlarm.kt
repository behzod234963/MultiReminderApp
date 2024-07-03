package coder.bekhzod

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@AndroidEntryPoint
class StopAlarm:BroadcastReceiver() {

    @Inject lateinit var alarmManager: AlarmManager
    @Inject lateinit var notificationManager:NotificationManagerCompat

    override fun onReceive(context: Context?, intent: Intent?) {

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notificationSound = RingtoneManager.getRingtone(context,ringtoneUri)
        val alarmIntent = Intent(context,NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,0,alarmIntent,
            PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        notificationManager.cancel(0)
        notificationSound.stop()


        Log.d("TAG", "onCreate: Alarm is cancelled")
    }
}