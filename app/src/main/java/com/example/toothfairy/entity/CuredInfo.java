package com.example.toothfairy.entity;


import com.example.toothfairy.util.DateManager;

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
public class CuredInfo {
    private int age;
    private int totalTreatmentDate;  // 총 치료 기간 (일 수)
    private Long totalWearTime;      // 총 착용 시간 (milliseconds)
    private Long maxWearingTime;     // 최대 착용 시간 (milliseconds)
    private Long minWearingTime;     // 최소 착용 시간 (milliseconds)

    /*
     * 총 착용 시간 / 총 치료 기간
     *   => 시간 당 교정 일 수 예측 가능
     * */

    public String getMaxWearingTimeToString(){
        return DateManager.getTimeToString(maxWearingTime);
    }

    public String getMinWearingTimeToString(){
        return DateManager.getTimeToString(minWearingTime);
    }

    public String getAvgWearingTimeToString(){

        return DateManager.getTimeToString(totalWearTime / totalTreatmentDate);
    }

    public Long getAvgWearingTime(){
        return totalWearTime / totalTreatmentDate;
    }
}
