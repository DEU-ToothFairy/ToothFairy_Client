package com.example.toothfairy.entity;


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
    private int totalTreatmentDate; // 총 치료 기간 (일 수)
    private Long totalWearTime;      // 총 착용 시간 (milliseconds)

    /*
     * 총 착용 시간 / 총 치료 기간
     *   => 시간 당 교정 일 수 예측 가능
     * */
}
