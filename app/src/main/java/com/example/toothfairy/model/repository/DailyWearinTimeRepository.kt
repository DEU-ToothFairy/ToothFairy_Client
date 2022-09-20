package com.example.toothfairy.model.repository

import com.example.toothfairy.dto.DailyWearTimeDto
import com.example.toothfairy.entity.DailyWearTime
import com.example.toothfairy.model.RetrofitClient
import com.example.toothfairy.model.service.DailyWearingTimeService
import com.example.toothfairy.model.service.PatientService
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

object DailyWearinTimeRepository {
    // VARIABLE
    private val dailyService: DailyWearingTimeService = RetrofitClient.retrofit.create(DailyWearingTimeService::class.java)

    fun saveDailyWearTimes(wearTimeLists:List<DailyWearTimeDto>):Response<List<DailyWearTime>?>?{
        val wearTimesCall: Call<List<DailyWearTime>?>? = dailyService.saveAll(wearTimeLists)

        return try{
            wearTimesCall?.execute()
        }catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
}