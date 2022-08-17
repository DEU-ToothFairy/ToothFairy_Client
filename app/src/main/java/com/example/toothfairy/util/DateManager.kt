package com.example.toothfairy.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import java.sql.Date

class DateManager {
    companion object {
        val today: String
            get() {
                val monthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" )
                val cal = Calendar.getInstance()
                val month = SimpleDateFormat("M").format(cal.time).toInt()
                val date = SimpleDateFormat("dd").format(cal.time).toInt()

                return date.toString() + " " + monthNames[month - 1]
            }

        // 특정 날짜로부터 지난 날짜 계산
        fun getElapsedDate(startDate: Date): Long {
            val cal = Calendar.getInstance()
            val elapsed = cal.timeInMillis - startDate.time

            // Day를 구해야하므로 24시간 * 60분 * 60초 * 1000 밀리세컨드 나눠주면 Day가 나옴
            return elapsed / (24 * 60 * 60 * 1000)
        }

        fun getTimeToString(time: Long?): String {
            Log.i("Time", "$time")

            if (time == null) return "0 시간 0 분"
            val hour = (time / (60 * 1000)).toInt() / 60
            val minute = (time / (60 * 1000)).toInt() % 60

            Log.i("Time To String", "${hour}시간 ${minute}분") // hour.toString() + "시간" + minute + "분"
            
            return " ${hour}시간 ${minute}분" //" " + hour + "시간 " + minute + "분 "
        }
    }
}