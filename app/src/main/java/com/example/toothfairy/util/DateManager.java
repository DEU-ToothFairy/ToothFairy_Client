package com.example.toothfairy.util;


import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class DateManager {
    public static String getToday(){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        Calendar cal = Calendar.getInstance();

        int month = Integer.parseInt(new SimpleDateFormat("M").format(cal.getTime()));
        int date = Integer.parseInt(new SimpleDateFormat("dd").format(cal.getTime()));

        return date + " " + monthNames[month - 1];
    }

    // 특정 날짜로부터 지난 날짜 계산
    public static Long getElapsedDate(Date startDate){
        Calendar cal = Calendar.getInstance();

        Long elapsed = (cal.getTimeInMillis() - startDate.getTime());

        // Day를 구해야하므로 24시간 * 60분 * 60초 * 1000 밀리세컨드 나눠주면 Day가 나옴
        return elapsed / (24 * 60 * 60 * 1000);
    }

    public static String getTimeToString(Long time){
        Log.i("Time", time + "");

        if(time == null) return "0 시간 0 분";

        int hour = (int)(time / (60 * 1000)) / 60;
        int minute = (int)(time / (60 * 1000)) % 60;

        Log.i("Time To String", hour + "시간" + minute + "분");
        return " " + hour + "시간 " + minute + "분 ";
    }
}
