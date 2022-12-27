package com.example.toothfairy.model

import com.example.toothfairy.util.ApiLogger
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    // 기본 URL
    private const val BASE_URL = "http://113.198.236.100:8080" //"http://220.92.179.244:8080";
    var retrofit: Retrofit

    init{
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()

        val interceptor = HttpLoggingInterceptor(ApiLogger())

        // Http 요청/응답 중 Body만 로깅
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }
}