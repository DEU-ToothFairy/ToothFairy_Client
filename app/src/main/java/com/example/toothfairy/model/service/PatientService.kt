package com.example.toothfairy.model.service

import retrofit2.http.GET
import com.example.toothfairy.entity.Patient
import retrofit2.http.POST
import com.example.toothfairy.dto.LoginDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Path

interface PatientService {
    @GET("api/patients/{id}")
    fun findByPatientNum(@Path("id") id: String?): Call<Patient?>?

    @POST("api/patients/login")
    fun login(@Body loginDto: LoginDto?): Call<Patient?>?
}