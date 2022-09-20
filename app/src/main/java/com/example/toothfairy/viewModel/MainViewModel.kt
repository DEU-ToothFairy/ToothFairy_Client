package com.example.toothfairy.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toothfairy.data.WearingStats
import com.example.toothfairy.dto.DailyWearTimeDto
import com.example.toothfairy.entity.CuredInfo
import com.example.toothfairy.entity.DailyWearTime
import com.example.toothfairy.entity.Patient
import com.example.toothfairy.model.repository.CuredInfoRepository
import com.example.toothfairy.model.repository.DailyWearinTimeRepository
import com.example.toothfairy.model.repository.PatientRepository
import com.example.toothfairy.model.repository.WearingInfoRepository
import com.example.toothfairy.util.TimeManager
import org.json.JSONArray
import retrofit2.Response
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/** 여러 Fragment에서 공유 되어야하는 데이터는 MainViewModel에서 유지 */
class MainViewModel() : ViewModel() {

    // VARIABLE
    var patient = MutableLiveData<Patient>()                // 환자 정보
    var curedInfo = MutableLiveData<CuredInfo>()            // 완치자 정보

    var treatmentDays = MutableLiveData<Long>()             // 교정 일 수
    var calibrationProgress = MutableLiveData<Double>()     // 교정 진행률

    var patientStats = MutableLiveData<WearingStats>()      // 환자 착용 통계

    var dailyWearingTime = MutableLiveData<Long>(WearingInfoRepository.dailyWearingTime)    // 당일 착용 시간
    val dailyWearingTimeToString: String
        get() = TimeManager.getTimeToString(dailyWearingTime.value)

    /** 환자의 ID로 환자의 정보를 가져오는 메소드 */
    fun loadPatient(id: String?) {
        Executors.newSingleThreadExecutor().execute {
            val response: Response<Patient?>? = PatientRepository.loadPatient(id)

            if (response!!.isSuccessful) {
                patient.postValue(response.body())

                // 환자의 ID로 내부 DB 초기화
                WearingInfoRepository.init(response.body()!!.id)
                Log.i("환자 정보 로드 성공", "Response [Patient] = ${response.body().toString()}")
            }
            else{
                Log.e("환자 정보 로드 에러", response.message())
            }
        }
    }

    /** age를 매개변수로 받아 해당 나이의 완치자 정보를 가져옴 */
    fun loadCuredInfo(age: Int) {
        Executors.newSingleThreadExecutor().execute {
            val response:Response<CuredInfo?>? = CuredInfoRepository.loadCuredInfo(age)

            if (response!!.isSuccessful) {
                curedInfo.postValue(response.body())
                Log.i("완치 환저 정보 로드 성공", "Reponse [CuredInfo] : ${response.body().toString()}")
            }
            else{
                Log.e("완치 환자 정보 로드 에러", response.message())
            }
        }
    }

    /** 착용 통계 데이터를 로드하는 메소드 */
    fun loadWearingStats() {
        dailyWearingTime.value = WearingInfoRepository.dailyWearingTime
        patientStats.value = WearingInfoRepository.wearingStats
    }

    fun sendSavedWearingTimes(){
        Executors.newSingleThreadExecutor().execute {
            val response:Response<List<DailyWearTime>?>? = DailyWearinTimeRepository.saveDailyWearTimes(getSavedWearingTimes())

            if (response!!.isSuccessful) {
                /** 요청 성공시 저장되어있던 착용 시간 데이터 삭제 */
                WearingInfoRepository.clearSavedWearingTimes()
                
                Log.i("착용 시간 전송 완료", "Response [List<DailyWearTimes>] = ${response.body().toString()}")
            }
            else{
                Log.e("착용 시간 전송 에러", response.message())
            }
        }
    }

    /** 시간을 전달받아 당일 착용 시간에 저장하는 메소드 */
    fun setDailyWearingTime(time: Long) {
        Log.i("SAVED", "시간 저장 됨")
        dailyWearingTime.value = WearingInfoRepository.setDailyWearingTime(time)
    }


    fun getSavedWearingTimes(): List<DailyWearTimeDto>? {
        val savedTime = WearingInfoRepository.savedWearTime
        if(savedTime == "") return null

        var jsonArray: JSONArray = JSONArray(savedTime)

        var wearingDatas: MutableList<DailyWearTimeDto> = mutableListOf()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        for (i in 0 until jsonArray.length()) {
            val data = jsonArray.getJSONObject(i)

            wearingDatas.add(
                    DailyWearTimeDto(
                        patient.value?.patientId!!,
                        java.sql.Date(dateFormat.parse(data["date"].toString()).time),
                        data["time"].toString().toLong()
                    )
            )
        }

        return wearingDatas
    }

    /** 교정 장치 착용 상태일 때를 처리하는 메소드 */
    fun detectedOn() {
        WearingInfoRepository.setOn()
    }

    /** 교정 장치 착용 해제되었을 때를 처리하는 메소드 */
    fun detectedOff() {
        // 착용 시간을 내부 DB에 저장하고 값 갱신
        dailyWearingTime.value = WearingInfoRepository.setOff()
    }

    /** 당일 착용 시간으로 환자 통계 그래프(평균, 최대, 최소) 데이터 갱신 */
    fun updatePatientStats(time: Long) {
        val avg = WearingInfoRepository.setAvgWearingTime(time)
        val max = WearingInfoRepository.setMaxWearingTime(time)
        val min = WearingInfoRepository.setMinWearingTime(time)
        patientStats.value = WearingStats(avg, max, min)
    }


}