package com.example.toothfairy.model.service

import android.graphics.Bitmap
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FaceDetectService {

    @Multipart
    @POST("upload/{userId}")
    fun sendBitmap(@Path("userId") userId:String, @Part file:MultipartBody.Part?): Call<ResponseBody>

    @Multipart
    @POST("upload2/{userId}")
    fun sendToothBrush(@Path("userId") userId:String, @Part file:MultipartBody.Part?): Call<ResponseBody>
}