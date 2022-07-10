package com.example.toothfairy.model.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Client가 Repository가 됨
@Getter
public class RetrofitClient {
    // 싱글턴 패턴
    private static RetrofitClient retrofitClient = new RetrofitClient();
    public static RetrofitClient getInstance(){ return retrofitClient; }
    private static RetrofitAPI retrofitAPI;

    // 기본 URL
    private final static String BASE_URL = "http://220.92.179.244:8080";

    private RetrofitClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .build();

        // retrofitAPI가 Service 부분
        // 매핑되는 Repository에서 Service를 생성해줘야 할 듯
        retrofitAPI = retrofit.create(RetrofitAPI.class);
    }

    public static RetrofitAPI getRetrofitAPI(){
        return retrofitAPI;
    }
}
