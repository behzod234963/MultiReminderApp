package coder.bekhzod

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) :ViewModel() {

    private val _dateAndTime = mutableLongStateOf(0L)
    val dateAndTime:State<Long> = _dateAndTime

    private val _date = mutableLongStateOf(0L)
    val date :State<Long> = _date

    private val _time = mutableLongStateOf(0L)
    val time :State<Long> = _time

    fun saveDate(date:Long){
        _date.longValue = date
    }

    fun saveTime(time:Long){
        _time.longValue = time
    }

    fun saveDateAndTime(){
        _dateAndTime.longValue = date.value+time.value
    }
}