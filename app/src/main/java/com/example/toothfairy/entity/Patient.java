package com.example.toothfairy.entity;


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
    private String patientNum;
    private String id;
    private String password;
    private String name;
    private Date birthDate;
    private Date startDate;
    private Date endDate;
    private List<DailyWearTime> dailyWearTimeList; // 환자의 일별 착용 시간 리스트
}
