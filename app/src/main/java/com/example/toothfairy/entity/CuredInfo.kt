package com.example.toothfairy.entity

import com.example.toothfairy.util.DateManager

class CuredInfo {
    var age = 0
        private set
    var totalTreatmentDate = 0// 총 치료 기간 (일 수)
        private set
    var totalWearTime: Long? = null // 총 착용 시간 (milliseconds)
        private set
    var maxWearingTime: Long? = null // 최대 착용 시간 (milliseconds)
        private set
    var minWearingTime: Long? = null // 최소 착용 시간 (milliseconds)
        private set

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
        get() = DateManager.getTimeToString(maxWearingTime)
    val minWearingTimeToString: String
        get() = DateManager.getTimeToString(minWearingTime)
    val avgWearingTimeToString: String
        get() = DateManager.getTimeToString(totalWearTime!! / totalTreatmentDate)
    val avgWearingTime: Long
        get() = totalWearTime!! / totalTreatmentDate

    override fun toString(): String {
        return "CuredInfo(age=$age, totalTreatmentDate=$totalTreatmentDate, totalWearTime=$totalWearTime, maxWearingTime=$maxWearingTime, minWearingTime=$minWearingTime)"
    }
}