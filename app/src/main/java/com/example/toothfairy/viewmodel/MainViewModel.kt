package com.example.toothfairy.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toothfairy.data.WearingStats
import com.example.toothfairy.entity.CuredInfo
import com.example.toothfairy.entity.Patient
import com.example.toothfairy.model.repository.CuredInfoRepository
import com.example.toothfairy.model.repository.PatientRepository
import com.example.toothfairy.model.repository.WearingInfoRepository
import com.example.toothfairy.util.DateManager
import retrofit2.Response
import java.util.concurrent.Executors

class MainViewModel : ViewModel() {
    // VARIABLE
    var patient = MutableLiveData<Patient?>()               // 환자 정보
    var curedInfo = MutableLiveData<CuredInfo?>()           // 완치자 정보

    var today = MutableLiveData<String>()                   // 오늘 날짜
    var treatmentDays = MutableLiveData<Long>()             // 교정 일 수
    var calibrationProgress = MutableLiveData<Double>()     // 교정 진행률

    var patientStats = MutableLiveData<WearingStats>()      // 환자 착용 통계
    var dailyWearingTime = MutableLiveData<Long>()          // 당일 착용 시간

    // 특정 환자의 정보를 가져옴
    fun loadPatient(id: String?) {
        Executors.newSingleThreadExecutor().execute {
            val response: Response<Patient?>? = PatientRepository.loadPatient(id)

            if (response!!.isSuccessful) {
                patient.postValue(response.body())

                // 환자의 ID로 내부 DB 초기화
                WearingInfoRepository.init(response.body()!!.id)
                Log.i("Response [Patient]", response.body().toString())
            }
        }
    }

    // 특정 나이의 완치자 정보를 가져옴
    fun loadCuredInfo(age: Int) {
        Executors.newSingleThreadExecutor().execute {
            val response:Response<CuredInfo?>? = CuredInfoRepository.loadCuredInfo(age)

            if (response!!.isSuccessful) {
                curedInfo.postValue(response.body())
                Log.i("Reponse [CuredInfo]", response.body().toString())
            }
        }
    }

    // 착용 통계 데이터 로드
    fun loadWearingStats() {
        dailyWearingTime.value = WearingInfoRepository.dailyWearingTime
        patientStats.value = WearingInfoRepository.wearingStats
    }

    // 교정 장치 착용 되었을 때
    fun detectedOn() {
        WearingInfoRepository.setOn()
    }

    // 교정 장치 착용 해제되었을 때
    fun detectedOff() {
        // 착용 시간을 내부 DB에 저장하고 값 갱신
        dailyWearingTime.value = WearingInfoRepository.setOff()
    }

    // 환자 통계 그래프 데이터 갱신
    fun updatePatientStats(time: Long) {
        val avg = WearingInfoRepository.setAvgWearingTime(time)
        val max = WearingInfoRepository.setMaxWearingTime(time)
        val min = WearingInfoRepository.setMinWearingTime(time)
        patientStats.value = WearingStats(avg, max, min)
    }

    fun setDailyWearingTime(time: Long) {
        Log.i("SAVED", "시간 저장 됨")
        dailyWearingTime.value = WearingInfoRepository.setDailyWearingTime(time)
    }

    val dailyWearingTimeToString: String
        get() = DateManager.getTimeToString(dailyWearingTime.value)
}