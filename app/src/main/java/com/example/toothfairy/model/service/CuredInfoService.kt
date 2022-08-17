package com.example.toothfairy.model.service

import retrofit2.http.GET
import com.example.toothfairy.entity.CuredInfo
import retrofit2.Call
import retrofit2.http.Path

interface CuredInfoService {
    @GET("api/cured/{age}")
    fun getCuredInfo(@Path("age") age: Int): Call<CuredInfo?>?
}