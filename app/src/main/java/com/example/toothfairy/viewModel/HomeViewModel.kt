package com.example.toothfairy.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toothfairy.util.TimeManager
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class HomeViewModel : ViewModel() {
    // 남은 착용 시간 (목표 착용 시간 - 일일 착용 시간으로 계산)
    // 목표 착용 시간(사용자가 설정에서 변경할 수 있도록 함) -> 목표 시간을 잘 지킬 경우 목표 시간을 늘리라는 알림도 주면 좋을 듯
    var targetWearingTime = MutableLiveData<Long>(TimeManager.parseMillisecond(16.5f))
    var remainWearingTime = MutableLiveData<Long>(0L)

    fun today():String{
        val localDate = LocalDate.now()
        var week = localDate.dayOfWeek.toString()
        week = week.substring(0, 1) + week.substring(1).toLowerCase()

        val day = localDate.dayOfMonth
        var month = localDate.month.toString()

        month = month.substring(0,1) + month.substring(1,3).toLowerCase()

        return "$week, $day $month"
    }

    fun updateRemainTime(dailyWearingTime: Long){
        remainWearingTime.value = targetWearingTime.value?.minus(dailyWearingTime)
    }

    fun remainWearingTimeToString():String {
        val remain = remainWearingTime.value

        if(remain!! < 0L) return "목표 시간 달성 !"

        val hour = remain?.let { TimeManager.getHour(it) }
        val minute = remain?.let { TimeManager.getMinutes(it) }

        return "$hour 시간 $minute 분 남음"
    }
}
