package com.example.toothfairy.model.repository;

import com.example.toothfairy.entity.Patient;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @GET("api/patients/{patientNum}")
    Call<Patient> getPatient(@Path("patientNum") String patientNum);
}
