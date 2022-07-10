package com.example.toothfairy.viewModel;

import android.icu.util.Calendar;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toothfairy.model.repository.RetrofitAPI;
import com.example.toothfairy.model.repository.RetrofitClient;
import com.example.toothfairy.model.util.DateManager;
import com.example.toothfairy.entity.CuredInfo;
import com.example.toothfairy.entity.Patient;
import com.example.toothfairy.model.repository.PatientRepository;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Setter
@Getter
public final class HomeFragmentViewModel extends ViewModel {

    private PatientRepository patientRepository = PatientRepository.getInstance();

    // VARIABLE
    private MutableLiveData<Patient> patient = new MutableLiveData<>();             // 환자 정보
    private MutableLiveData<List<Patient>> patientList = new MutableLiveData<>();   // 환자 정보 리스트
    private MutableLiveData<CuredInfo> curedInfo = new MutableLiveData<>();         // 완치자 정보

    private MutableLiveData<String> today = new MutableLiveData<>();                // 오늘 날짜
    private MutableLiveData<Long> treatmentDays = new MutableLiveData<>();          // 교정 일 수
    private MutableLiveData<Double> calibrationProgress = new MutableLiveData<>(); // 교정 진행률

    public HomeFragmentViewModel(){
        setPatient("000001");

        curedInfo.setValue(CuredInfo.builder()
                                    .age(9)
                                    .totalTreatmentDate(549)
                                    .totalWearTime(Long.valueOf(7686 * 60 * 1000))
                                    .build());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 01, 01);

        // 오늘 날짜 설정
        today.setValue(DateManager.getToday());
        // 치료 기간 설정
        treatmentDays.setValue(DateManager.getElapsedDate(new Date(calendar.getTime().getTime())));
        
        double progress = Math.round((treatmentDays.getValue() / (double)curedInfo.getValue().getTotalTreatmentDate()) * 100);

        Log.i("교정 진행 률 : ", progress + "");
        // 교정 진행률 설정
        calibrationProgress.setValue(progress);

    }

    public void setPatient(String patientNum) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        if(retrofitClient != null){
            RetrofitAPI retrofitAPI = RetrofitClient.getRetrofitAPI();

            Executors.newSingleThreadExecutor().execute(()->{
                try {
                    Response<Patient> response = retrofitAPI.getPatient(patientNum).execute();

                    patient.postValue(response.body());
                    Log.i("Response", response.body().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        }

//        Log.i("Response", patient.getValue().toString());
    }

//    public void setAllPatient(){
//        Executors.newSingleThreadExecutor().execute(()->{
//            patientList.setValue(patientRepository.getAllPatients());
//        });
//    }
}
