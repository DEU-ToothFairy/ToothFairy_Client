package com.example.toothfairy.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import java.sql.Date
import java.util.concurrent.TimeUnit

class TimeManager {
    companion object {
        val today: String
            get() {
                val monthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" )
                val cal = Calendar.getInstance()
                val month = SimpleDateFormat("M").format(cal.time).toInt()
                val date = SimpleDateFormat("dd").format(cal.time).toInt()

                return date.toString() + " " + monthNames[month - 1]
            }

        /** 전달 받은 날짜로부터 지난 날짜를 계산(교정 일 수를 계산)하는 메소드 */
        fun getElapsedDate(startDate: Date): Long {
            val cal = Calendar.getInstance()
            val elapsed = cal.timeInMillis - startDate.time

            // Day를 구해야하므로 24시간 * 60분 * 60초 * 1000 밀리세컨드 나눠주면 Day가 나옴
            return elapsed / (24 * 60 * 60 * 1000)
        }

        /** Long 타입의 시간을 "시간 H 분 m" 형태로 반환하는 메소드 */
        fun getTimeToString(time: Long?): String {
            var time = time
            if (time == null) time = 0

            val hour = (time / (60 * 1000)).toInt() / 60
            val minute = (time / (60 * 1000)).toInt() % 60

            Log.i("Time To String", "${hour}시간 ${minute}분") // hour.toString() + "시간" + minute + "분"
            
            return "%02d H %02d m".format(hour, minute) //" " + hour + "시간 " + minute + "분 "
        }

        /** Float로 시간을 받아 Long(밀리세컨드)로 변경해주는 메소드 */
        fun parseMillisecond(time:Float): Long{
            var hour = time.toInt()
            var minute = (time - time.toInt()) * 60 // 0.5면 * 60해서 30분

            return ((hour * 60 * 60 * 1000) + (minute * 60 * 1000)).toLong()
        }

        /** 밀리세컨드를 받아 시간을 반환하는 메소드 */
        fun getHour(time:Long): Long{
            return TimeUnit.MILLISECONDS.toHours(time)
        }

        /** 밀리세컨드를 받아 분을 반환하는 메소드 HH:mm에서 mm 부분*/
        fun getMinutes(time:Long): Long{
            return TimeUnit.MILLISECONDS.toMinutes(time) % 60
        }
    }
}