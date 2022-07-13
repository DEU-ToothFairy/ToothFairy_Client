package com.example.toothfairy.entity;


import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Expose
    @SerializedName("patientNum")
    private String patientNum;

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("birthDate")
    private Date birthDate;

    @Expose
    @SerializedName("startDate")
    private Date startDate;

    @Expose
    @SerializedName("endDate")
    private Date endDate;

    @Expose
    @SerializedName("dailyWearTimeList")
    private List<DailyWearTime> dailyWearTimeList; // 환자의 일별 착용 시간 리스트
    // 안되면 DailyWearTime[]으로 바꾸기

    public int getAge(){
        return (int)((startDate.getTime() - birthDate.getTime()) / (1000L * 365L * 60L * 60L * 24L));
    }
}
