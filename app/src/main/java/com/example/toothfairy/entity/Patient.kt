package com.example.toothfairy.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.example.toothfairy.entity.DailyWearTime
import java.sql.Date

class Patient {
    @Expose
    @SerializedName("patientNum")
    var patientNum: String? = null

    @Expose
    @SerializedName("id")
    var id: String? = null

    @Expose
    @SerializedName("password")
    var password: String? = null

    @Expose
    @SerializedName("name")
    var name: String? = null

    @Expose
    @SerializedName("birthDate")
    var birthDate: Date? = null

    @Expose
    @SerializedName("startDate")
    var startDate: Date? = null

    @Expose
    @SerializedName("endDate")
    var endDate: Date? = null

    @Expose
    @SerializedName("dailyWearTimeList") // 안되면 DailyWearTime[]으로 바꾸기
    var dailyWearTimeList: List<DailyWearTime>? = null // 환자의 일별 착용 시간 리스트

    constructor(
        patientNum: String?,
        id: String?,
        password: String?,
        name: String?,
        birthDate: Date?,
        startDate: Date?,
        endDate: Date?,
        dailyWearTimeList: List<DailyWearTime>?
    ) {
        this.patientNum = patientNum
        this.id = id
        this.password = password
        this.name = name
        this.birthDate = birthDate
        this.startDate = startDate
        this.endDate = endDate
        this.dailyWearTimeList = dailyWearTimeList
    }

    constructor() {}

    val age: Int
        get() = ((startDate!!.time - birthDate!!.time) / (1000L * 365L * 60L * 60L * 24L)).toInt()

    override fun toString(): String {
        return "Patient(patientNum=$patientNum, id=$id, password=$password, name=$name, birthDate=$birthDate, startDate=$startDate, endDate=$endDate, dailyWearTimeList=$dailyWearTimeList)"
    }
}