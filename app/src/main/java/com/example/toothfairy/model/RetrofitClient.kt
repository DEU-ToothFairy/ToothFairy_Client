package com.example.toothfairy.model

import retrofit2.Retrofit
import com.example.toothfairy.model.RetrofitClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import lombok.Getter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@Getter
object RetrofitClient {
    // 기본 URL
    private const val BASE_URL = "http://113.198.236.99:8080" //"http://220.92.179.244:8080";
    var retrofit: Retrofit

    init{
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

//    // retrofitAPI가 Service 부분
//    val instance: Retrofit?
//        get() {
//            if (Objects.isNull(retrofit)) {
//                val gson = GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd")
//                    .create()
//
//                retrofit = Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .build()
//            }
//            return retrofit
//        }

    // 매핑되는 Repository에서 Service를 생성해줘야 할 듯
    //        retrofitAPI = retrofit.create(RetrofitAPI.class);
}