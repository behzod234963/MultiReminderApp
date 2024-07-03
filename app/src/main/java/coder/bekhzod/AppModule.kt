package coder.bekhzod

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @SuppressLint("ServiceCast")
    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext ctx : Context):NotificationManagerCompat{
        val notificationManager = NotificationManagerCompat.from(ctx)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "Main Channel ID",
                "Main Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext ctx: Context):AlarmManager{
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return alarmManager
    }

    @Provides
    @Singleton
    fun provideNotificationScheduler(notificationManager: NotificationManagerCompat,alarmManager:AlarmManager):NotificationScheduler{
        return NotificationScheduler(notificationManager,alarmManager)
    }
}