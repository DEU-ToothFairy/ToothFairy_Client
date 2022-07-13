package com.example.toothfairy.viewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toothfairy.data.WearingStats;
import com.example.toothfairy.model.repository.CuredInfoRepository;
import com.example.toothfairy.entity.CuredInfo;
import com.example.toothfairy.entity.Patient;
import com.example.toothfairy.model.repository.PatientRepository;
import com.example.toothfairy.model.repository.WearingInfoRepository;
import com.example.toothfairy.util.DateManager;

import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;


@Setter
@Getter
public final class MainViewModel extends ViewModel {

    // REPOSITORY
    private PatientRepository patientRepository = PatientRepository.getInstance();
    private CuredInfoRepository curedInfoRepository = CuredInfoRepository.getInstance();
    private WearingInfoRepository wearingInfoRepository = WearingInfoRepository.getInstance();


    // VARIABLE
    private MutableLiveData<Patient> patient = new MutableLiveData<>();             // 환자 정보
    private MutableLiveData<CuredInfo> curedInfo = new MutableLiveData<>();         // 완치자 정보

    private MutableLiveData<String> today = new MutableLiveData<>();                // 오늘 날짜
    private MutableLiveData<Long> treatmentDays = new MutableLiveData<>();          // 교정 일 수
    private MutableLiveData<Double> calibrationProgress = new MutableLiveData<>();  // 교정 진행률

    private MutableLiveData<WearingStats> patientStats = new MutableLiveData<>();   // 환자 착용 통계
    private MutableLiveData<Long> dailyWearingTime = new MutableLiveData<>();       // 당일 착용 시간

    public MainViewModel() { }

    // 특정 환자의 정보를 가져옴
    public void loadPatient(String id) {
        Executors.newSingleThreadExecutor().execute(()->{
            Response<Patient> response = patientRepository.loadPatient(id);

            if(response.isSuccessful()){
                patient.postValue(response.body());

                // 환자의 ID로 내부 DB 초기화
                wearingInfoRepository.init(response.body().getId());

                Log.i("Response [Patient]", response.body().toString());
            }
        });
    }

    // 특정 나이의 완치자 정보를 가져옴
    public void loadCuredInfo(int age){
        Executors.newSingleThreadExecutor().execute(() -> {
            Response<CuredInfo> response = curedInfoRepository.loadCuredInfo(age);

            if(response.isSuccessful()){
                curedInfo.postValue(response.body());

                Log.i("Reponse [CuredInfo]", response.body().toString());
            }
        });
    }

    // 착용 통계 데이터 로드
    public void loadWearingStats(){
        dailyWearingTime.setValue(wearingInfoRepository.getDailyWearingTime());
        patientStats.setValue(wearingInfoRepository.getWearingStats());
    }

    // 교정 장치 착용 되었을 때
    public void detectedOn() { wearingInfoRepository.setOn(); }
    // 교정 장치 착용 해제되었을 때
    public void detectedOff(){
        // 착용 시간을 내부 DB에 저장하고 값 갱신
        dailyWearingTime.setValue(wearingInfoRepository.setOff());
    }

    // 환자 통계 그래프 데이터 갱신
    public void updatePatientStats(Long time){
        Long avg = wearingInfoRepository.setAvgWearingTime(time);
        Long max = wearingInfoRepository.setMaxWearingTime(time);
        Long min = wearingInfoRepository.setMinWearingTime(time);

        this.patientStats.setValue(new WearingStats(avg,max,min));
    }

    public String getDailyWearingTimeToString(){
        return DateManager.getTimeToString(dailyWearingTime.getValue());
    }


}

