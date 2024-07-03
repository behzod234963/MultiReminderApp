package coder.bekhzod

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.STREAM_DEFAULT
import androidx.core.app.NotificationCompat.StreamType
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    private val notificationManager: NotificationManagerCompat,
    private val alarmManager: AlarmManager
) {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(ctx: Context, delay:Long){

        Log.d("TAG", "onCreate: function scheduleNotification is started")
        val alarmIntent = Intent(ctx,NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(ctx,0,alarmIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val alarmTime = System.currentTimeMillis() + delay

        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun showNotification(ctx:Context, title:String, content:String){

        Log.d("TAG", "onCreate: function showNotification is started")
        val contentIntent = Intent(ctx,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                PendingIntent.FLAG_IMMUTABLE
            else
                0
        val contentIntentPendingIntent = PendingIntent.getActivity(ctx,2,contentIntent, PendingIntent.FLAG_IMMUTABLE)
        val stopAlarmIntent = Intent(ctx,StopAlarm::class.java)
        val stopAlarmPendingIntent = PendingIntent.getBroadcast(ctx,1,stopAlarmIntent,flag)
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notificationSound = RingtoneManager.getRingtone(ctx,ringtoneUri)
        val notification =  NotificationCompat.Builder(ctx,"Main Channel ID")
            .setOngoing(true)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(ringtoneUri.normalizeScheme(), AudioManager.STREAM_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(contentIntentPendingIntent)
            .addAction(0,"Stop",stopAlarmPendingIntent)

        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(0,notification.build()).apply {
                notificationSound.stop()
                notificationSound.isLooping = true
                notificationSound.play()
            }
        }
    }
}