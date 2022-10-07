package com.example.toothfairy.viewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toothfairy.application.MyApplication
import com.example.toothfairy.dto.LoginDto
import com.example.toothfairy.model.repository.PatientRepository
import com.example.toothfairy.model.repository.WearingInfoRepository
import com.example.toothfairy.util.Event
import java.util.concurrent.Executors

class LoginViewModel : ViewModel() {
    val loginFlag = MutableLiveData<Boolean>()
    val error = MutableLiveData<Event<String>>()

    /** 아이디와 비밀 번호를 받아 로그인하는 메소드 */
    fun login(id: String, password: String) {
        // 이미 저장되어 있는 정보가 있으면 자동 로그인
        Executors.newSingleThreadExecutor().execute {
            // 로그인 요청
            val response = PatientRepository.login(LoginDto(id, password))
            
            if (response!!.isSuccessful) {
                val patient = response.body()

                loginFlag.postValue(true)
                MyApplication.patient = patient

                // 사용자 이름의 DB 생성
                WearingInfoRepository.init(patient!!.patientId)
                
                Log.i("로그인 성공", "로그인 사용자 = $patient, Message = ${response.message()}")
            } else {
                response.errorBody()?.let { Log.e("로그인 실패", it.string()) }
                error.postValue(Event("입력 값을 확인해주세요."))
            }
        }
    }

    // 로그인 정보 저장
    fun saveLoginData(sharedPreferences: SharedPreferences, userId: String?, password: String?) {
        val autoLoginEdit = sharedPreferences.edit()

        autoLoginEdit.putString("id", userId)
        autoLoginEdit.putString("password", password)
        autoLoginEdit.commit()
    }

    // 자동 로그인
    fun autoLogin(sharedPreferences: SharedPreferences): Boolean {
        val id = sharedPreferences.getString("id", null) // id가 없으면 null 반환
        val password = sharedPreferences.getString("password", null)
        
        if (id != null || password != null) {
            login(id!!, password!!)
            return true
        }
        return false
    }
}