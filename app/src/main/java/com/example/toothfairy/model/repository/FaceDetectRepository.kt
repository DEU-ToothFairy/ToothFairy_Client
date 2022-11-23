package com.example.toothfairy.model.repository

import android.graphics.Bitmap
import com.example.toothfairy.model.RetrofitClient
import com.example.toothfairy.model.service.FaceDetectService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.BufferedSink
import retrofit2.Call


object FaceDetectRepository {
    val faceDetectService = RetrofitClient.retrofit.create(FaceDetectService::class.java)

    /**
     * [ 옆면 얼굴 인식 ]
     * 서버로 비트맵을 전송하는 메소드
     */
    fun sendBitmap(userId:String, bitmap:Bitmap): Call<ResponseBody> {
        val bitmapRequestBody = BitmapRequestBody(bitmap)
        val bitmapMultipartBody =
            if(bitmapRequestBody == null) null
            else MultipartBody.Part.createFormData("file", "sideface", bitmapRequestBody)

        return faceDetectService.sendBitmap(userId, bitmapMultipartBody)
    }

    /**
     * [ 칫솔모 교체 판별 ]
     * 서버로 비트맵을 전송하는 메소드
     */
    fun sendToothBrush(userId:String, bitmap:Bitmap): Call<ResponseBody> {
        val bitmapRequestBody = BitmapRequestBody(bitmap)
        val bitmapMultipartBody =
            if(bitmapRequestBody == null) null
            else MultipartBody.Part.createFormData("file", "sideface", bitmapRequestBody)

        return faceDetectService.sendToothBrush(userId, bitmapMultipartBody)
    }

    class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/jpeg".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, sink.outputStream())
        }
    }
}