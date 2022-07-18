package com.example.toothfairy.viewModel;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.toothfairy.dto.LoginDto;
import com.example.toothfairy.entity.Patient;
import com.example.toothfairy.model.repository.PatientRepository;
import com.example.toothfairy.util.Event;

import java.util.concurrent.Executors;

import lombok.Getter;
import retrofit2.Response;

@Getter
public class LoginViewModel extends ViewModel {
    private PatientRepository patientRepository = PatientRepository.getInstance();

    public static LoginViewModel loginViewModel = new LoginViewModel();
    public static LoginViewModel getInstance(){ return loginViewModel; }
    private MutableLiveData<String> loginUser = new MutableLiveData<>();
    private MutableLiveData<Event<String>> error = new MutableLiveData<>();

    private LoginViewModel() {}

    // 기본 로그인 로직
    public void login(String id, String password){
        // 이미 저장되어 있는 정보가 있으면 자동 로그인
        Executors.newSingleThreadExecutor().execute(()->{
            Response<Patient> response = patientRepository.login(new LoginDto(id, password));

            if(response.isSuccessful()){
                Patient patient = response.body();
                loginUser.postValue(patient.getId());

                Log.i("Login", patient.toString());
            }
            else {
                Log.e("Login Error", "로그인 실패");
                this.error.postValue(new Event<>("입력 값을 확인해주세요."));
            }
        });
    }
    
    
    // 로그인 정보 저장
    public void saveLoginData(SharedPreferences sharedPreferences, String userId, String password){
        SharedPreferences.Editor autoLoginEdit = sharedPreferences.edit();

        autoLoginEdit.putString("id", userId);
        autoLoginEdit.putString("password", password);

        autoLoginEdit.commit();
    }

    // 자동 로그인
    public boolean autoLogin(SharedPreferences sharedPreferences){
        String id = sharedPreferences.getString("id", null);
        String password = sharedPreferences.getString("password", null);

        if(id != null || password != null){
            login(id, password);

            return true;
        }

        return false;
    }
}
