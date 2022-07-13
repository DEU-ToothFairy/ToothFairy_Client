package com.example.toothfairy.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Getter
public class RetrofitClient {
    // 기본 URL
    private final static String BASE_URL = "http://113.198.236.99:8080"; //"http://220.92.179.244:8080";
    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(Objects.isNull(retrofit)) {
            Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd")
                            .create();

            retrofit = new Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();
        }

        return retrofit;
    }

        // retrofitAPI가 Service 부분
        // 매핑되는 Repository에서 Service를 생성해줘야 할 듯
//        retrofitAPI = retrofit.create(RetrofitAPI.class);
}
