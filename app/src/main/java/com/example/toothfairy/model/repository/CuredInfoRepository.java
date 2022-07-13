package com.example.toothfairy.model.repository;


import com.example.toothfairy.entity.CuredInfo;
import com.example.toothfairy.model.RetrofitClient;
import com.example.toothfairy.model.service.CuredInfoService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CuredInfoRepository {
    // 싱글턴 패턴
    public static CuredInfoRepository curedInfoRepository = new CuredInfoRepository();
    public static CuredInfoRepository getInstance(){ return curedInfoRepository; }

    // VARIABLE
    private CuredInfoService curedInfoService;

    private CuredInfoRepository(){
        Retrofit retrofit = RetrofitClient.getInstance();

        curedInfoService = retrofit.create(CuredInfoService.class);
    }

    // 완치 환자 정보 로드
    public Response<CuredInfo> loadCuredInfo(int age){
        Call<CuredInfo> curedInfoCall = curedInfoService.getCuredInfo(age);

        try {
            return curedInfoCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
