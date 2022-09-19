package com.example.toothfairy.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Date

class DailyWearTimeDto() {
    private lateinit var patientId: String
    private lateinit var wearDate: Date // 착용 일자
    private var totalWearTime: Long = 0 // 하루 총 착용 시간 (milliseconds)

    constructor(patientId: String, wearDate: Date, totalWearTime: Long): this() {
        this.patientId = patientId
        this.wearDate = wearDate
        this.totalWearTime = totalWearTime
    }
}