package com.example.toothfairy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toothfairy.util.DateManager
import java.time.LocalDate
import java.util.*

class HomeViewModel : MainViewModel() {
    fun today():String{
        val localDate = LocalDate.now()
        var week = localDate.dayOfWeek.toString()
        week = week.substring(0, 1) + week.substring(1).toLowerCase()

        val day = localDate.dayOfMonth
        var month = localDate.month.toString()

        month = month.substring(0,1) + month.substring(1).toLowerCase()

        return "$week, $day $month"
    }

}
