package com.example.toothfairy.model.service;

import com.example.toothfairy.entity.CuredInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CuredInfoService {
    @GET("api/cured/{age}")
    public Call<CuredInfo> getCuredInfo(@Path("age") int age);
}
