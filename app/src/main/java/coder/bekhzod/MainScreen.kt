package coder.bekhzod

import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.simulateHotReload
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.NotificationWithIdAndTag
import androidx.hilt.navigation.compose.hiltViewModel
import java.sql.Time
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    ctx: Context,
    notificationManager: NotificationManagerCompat,
    alarmManager: AlarmManager,
    viewModel: MainViewModel = hiltViewModel()
) {
    val localContext = LocalContext.current
    val activityContext = LocalContext.current as Activity

    val isDatePicked = remember { mutableStateOf( false ) }
    val isTimePicked = remember { mutableStateOf( false ) }

    val notificationScheduler = NotificationScheduler(notificationManager, alarmManager)

    val calendarInstance = Calendar.getInstance()
    val year = calendarInstance.get(Calendar.YEAR)
    val monthOfYear = calendarInstance.get(Calendar.MONTH)
    val dayOfMonth = calendarInstance.get(Calendar.DAY_OF_MONTH)
    val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
    val minute = calendarInstance.get(Calendar.MINUTE)

    val selectedDate = remember { mutableLongStateOf(0L) }
    val datePickerDialog = DatePickerDialog(
        ctx,
        /* i = year
        * i1 = month
        * i2 = day */
        { _: DatePicker, i: Int, i1: Int, i2: Int ->
            Unit
            val calendar = Calendar.getInstance()
            calendar.set(i, i1, i2)
            selectedDate.longValue = calendar.timeInMillis
        }, year, monthOfYear, dayOfMonth
    )
    val selectedTime = remember { mutableLongStateOf(0L) }
    val timePickerDialog = TimePickerDialog(
        ctx,
        { _, hourOfDay: Int, pickedMinute: Int ->
            selectedTime.longValue = "$hourOfDay$pickedMinute".toLong()
        }, hour, minute, true
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            datePickerDialog.show()
            viewModel.saveDate(selectedDate.longValue)
            isDatePicked.value = true
        }) {
            Text("Set date")
        }
        Button(onClick = {
            timePickerDialog.show()
            viewModel.saveTime(selectedTime.longValue)
            isTimePicked.value = true
        }) {
            Text("Set time")
        }

        Button(onClick = {
            if (isDatePicked.value || isTimePicked.value){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    ActivityCompat.requestPermissions(
                        activityContext,
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        0
                    )
                }
                notificationScheduler.scheduleNotification(activityContext, viewModel.dateAndTime.value)
            }else{
                Toast.makeText(activityContext, "Invalid date or time", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Set alarm")
        }
    }
}