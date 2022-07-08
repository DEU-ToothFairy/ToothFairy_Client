package com.example.toothfairy.entity;

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
    private int id;
    private String patientNum;
    private Date wearDate;           // 착용 일자
    private Long totalWearTime;      // 하루 총 착용 시간 (milliseconds)
}
