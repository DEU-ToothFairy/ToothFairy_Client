package com.example.toothfairy.model;

import androidx.lifecycle.MutableLiveData;

import com.example.toothfairy.entity.Patient;

import java.util.List;

public class PatientRepository {
    // 싱글턴 패턴
    public static PatientRepository patientRepository = new PatientRepository();
    public static PatientRepository getInstance(){ return patientRepository; }
    
    private PatientRepository() {}

    public MutableLiveData<List<Patient>> getPatient(){

        MutableLiveData<List<Patient>> data = new MutableLiveData<>(); //MutableLiveData 객체 생성

        return data; //MutableLiveData return
    }
}
