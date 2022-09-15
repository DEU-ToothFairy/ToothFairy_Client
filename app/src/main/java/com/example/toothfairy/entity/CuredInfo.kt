package com.example.toothfairy.entity

import com.example.toothfairy.util.TimeManager

class CuredInfo {
    var age = 0
    var totalTreatmentDate = 0// 총 치료 기간 (일 수)
    var totalWearTime: Long? = null // 총 착용 시간 (milliseconds)
    var maxWearingTime: Long? = null // 최대 착용 시간 (milliseconds)
    var minWearingTime: Long? = null // 최소 착용 시간 (milliseconds)

    constructor(
        age: Int,
        totalTreatmentDate: Int,
        totalWearTime: Long?,
        maxWearingTime: Long?,
        minWearingTime: Long?
    ) {
        this.age = age
        this.totalTreatmentDate = totalTreatmentDate
        this.totalWearTime = totalWearTime
        this.maxWearingTime = maxWearingTime
        this.minWearingTime = minWearingTime
    }

    constructor() {}

    /*
     * 총 착용 시간 / 총 치료 기간
     *   => 시간 당 교정 일 수 예측 가능
     * */
    val maxWearingTimeToString: String
        get() = TimeManager.getTimeToString(maxWearingTime)
    val minWearingTimeToString: String
        get() = TimeManager.getTimeToString(minWearingTime)
    val avgWearingTimeToString: String
        get() = TimeManager.getTimeToString(totalWearTime!! / totalTreatmentDate)
    val avgWearingTime: Long
        get() = totalWearTime!! / totalTreatmentDate

    override fun toString(): String {
        return "CuredInfo(age=$age, totalTreatmentDate=$totalTreatmentDate, totalWearTime=$totalWearTime, maxWearingTime=$maxWearingTime, minWearingTime=$minWearingTime)"
    }
}