package com.example.toothfairy.model.repository

import com.example.toothfairy.dto.ResponseDto.SideFaceResDto
import com.example.toothfairy.model.RetrofitClient
import com.example.toothfairy.model.service.SideFaceDetectService
import okio.IOException
import retrofit2.Call
import retrofit2.Response

object SideFaceDetectRepository {
    private val sideFaceService = RetrofitClient.retrofit.create(SideFaceDetectService::class.java)

    fun getDetectResult(): Response<SideFaceResDto.DetectResult>? {
        val sideFaceCall = sideFaceService.getSideFaceResult()

        return try{
            sideFaceCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
}