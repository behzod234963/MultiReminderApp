package coder.bekhzod

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

class CalendarUtils (
    private val ctx: Context
){

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCalendarProperties(day:Int,month:Int,year:Int):Long{

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_MONTH,day)
            set(Calendar.MONTH,month)
            set(Calendar.DAY_OF_MONTH,day)
        }
        return calendar.timeInMillis
    }
}