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
    private val curedInfoService: CuredInfoService

    // 완치 환자 정보 로드
    fun loadCuredInfo(age: Int): Response<CuredInfo?>? {
        val curedInfoCall: Call<CuredInfo?>? = curedInfoService.getCuredInfo(age)

        return try {
            curedInfoCall?.execute()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    init {
        // retrofit 객체 생성(싱글턴)
        val retrofit:Retrofit? = RetrofitClient.instance

        // service 객체 생성
        // curedInfoService가 nullable 변수가 아니므로 ?.를 사용할 수 없음
        curedInfoService = retrofit!!.create(CuredInfoService::class.java)
    }
}