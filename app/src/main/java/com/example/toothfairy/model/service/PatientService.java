package com.example.toothfairy.model.service;

import com.example.toothfairy.data.WearingTime;
import com.example.toothfairy.dto.LoginDto;
import com.example.toothfairy.entity.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PatientService {
    @GET("api/patients/{id}")
    Call<Patient> findByPatientNum(@Path("id") String id);

    @POST("api/patients/login")
    Call<Patient> login(@Body LoginDto loginDto);
}
