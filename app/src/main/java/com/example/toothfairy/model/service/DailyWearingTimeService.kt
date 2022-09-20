package com.example.toothfairy.model.service

import com.example.toothfairy.dto.DailyWearTimeDto
import com.example.toothfairy.entity.DailyWearTime
import com.example.toothfairy.entity.Patient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DailyWearingTimeService {
    @POST("api/dailyWearTimes")
    fun saveAll(@Body dailyWearTimes:List<DailyWearTimeDto>): Call<List<DailyWearTime>?>?
}