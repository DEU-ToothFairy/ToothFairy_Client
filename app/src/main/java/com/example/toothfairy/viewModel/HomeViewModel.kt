package com.example.toothfairy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toothfairy.util.DateManager
import java.time.LocalDate
import java.util.*

class HomeViewModel : MainViewModel() {
    // 남은 착용 시간 (목표 착용 시간 - 일일 착용 시간으로 계산)
    var remainWearingTime = MutableLiveData<Long>(
        dailyWearingTime.value?.let {
            targetWearingTime.value?.minus(it.toLong())
        }
    )

    fun today():String{
        val localDate = LocalDate.now()
        var week = localDate.dayOfWeek.toString()
        week = week.substring(0, 1) + week.substring(1).toLowerCase()

        val day = localDate.dayOfMonth
        var month = localDate.month.toString()

        month = month.substring(0,1) + month.substring(1).toLowerCase()

        return "$week, $day $month"
    }

    fun remainWearingTimeToString():String {
        val hour = remainWearingTime.value?.let { DateManager.getHour(it) }
        val minute = remainWearingTime.value?.let { DateManager.getMinutes(it) }

        return "$hour 시간 $minute 분 남음"
    }
}
