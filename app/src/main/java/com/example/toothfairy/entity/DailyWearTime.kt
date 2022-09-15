package com.example.toothfairy.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Date

class DailyWearTime {
    @Expose
    @SerializedName("id")
    var id = 0

    @Expose
    @SerializedName("patientNum")
    var patientNum: String? = null

    @Expose
    @SerializedName("wearDate")
    var wearDate: Date? = null // 착용 일자

    @Expose
    @SerializedName("totalWearTime")
    var totalWearTime: Long? = null // 하루 총 착용 시간 (milliseconds)

    constructor(id: Int, patientNum: String?, wearDate: Date?, totalWearTime: Long?): this() {
        this.id = id
        this.patientNum = patientNum
        this.wearDate = wearDate
        this.totalWearTime = totalWearTime
    }

    constructor() {}

    override fun toString(): String {
        return "DailyWearTime(id=$id, patientNum=$patientNum, wearDate=$wearDate, totalWearTime=$totalWearTime)"
    }
}