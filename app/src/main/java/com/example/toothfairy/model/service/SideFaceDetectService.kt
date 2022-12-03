package com.example.toothfairy.model.service

import com.example.toothfairy.dto.ResponseDto.SideFaceResDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface SideFaceDetectService {
    @GET("/faceinfo")
    fun getSideFaceResult(): Call<SideFaceResDto.DetectResult>
}