package com.example.toothfairy.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;

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
public class DailyWearTime {

    @Expose @SerializedName("id")
    private int id;

    @Expose @SerializedName("patientNum")
    private String patientNum;

    @Expose @SerializedName("wearDate")
    private Date wearDate;           // 착용 일자

    @Expose @SerializedName("totalWearTime")
    private Long totalWearTime;      // 하루 총 착용 시간 (milliseconds)
}
