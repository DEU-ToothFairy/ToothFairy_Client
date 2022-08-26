package com.example.toothfairy.model.repository

import com.example.toothfairy.model.service.CuredInfoService
import com.example.toothfairy.entity.CuredInfo
import com.example.toothfairy.model.repository.CuredInfoRepository
import retrofit2.Retrofit
import com.example.toothfairy.model.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.create
import java.io.IOException

object CuredInfoRepository {
    // VARIABLE
    // service 객체 생성
    private val curedInfoService: CuredInfoService = RetrofitClient.retrofit.create(CuredInfoService::class.java)

    /** 나이에 맞는 완치 환자의 정보를 가져오는 메소드 */
    fun loadCuredInfo(age: Int): Response<CuredInfo?>? {
        val curedInfoCall: Call<CuredInfo?>? = curedInfoService.getCuredInfo(age)

        return try {
            curedInfoCall?.execute()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}