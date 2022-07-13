package com.example.toothfairy.model.repository;

import com.example.toothfairy.dto.LoginDto;
import com.example.toothfairy.entity.DailyWearTime;
import com.example.toothfairy.entity.Patient;
import com.example.toothfairy.model.RetrofitClient;
import com.example.toothfairy.model.service.PatientService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PatientRepository{
    // 싱글턴 패턴
    public static PatientRepository patientRepository = new PatientRepository();
    public static PatientRepository getInstance(){ return patientRepository; }

    // VARIABLE
    private PatientService patientService;

    private PatientRepository() {
        Retrofit retrofit = RetrofitClient.getInstance();

        this.patientService = retrofit.create(PatientService.class);
    }

    // 환자 정보 가져오는 메소드
    public Response<Patient> loadPatient(String id){
        Call<Patient> patientCall = patientService.findByPatientNum(id);

        try {
            return patientCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response<Patient> login(LoginDto loginDto){
        Call<Patient> patientCall = patientService.login(loginDto);

        try {
            return patientCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    private Patient jsonToPatient(JSONObject patient) throws JSONException {
//        String patientNum = patient.getString("patientNum");
//        String id = patient.getString("id");
//        String password = patient.getString("password");
//        String name = patient.getString("name");
//        String birthDate = patient.getString("birthDate");
//        String startDate = patient.getString("startDate");
//        String endDate = patient.getString("endDate");
//        Object dailyWearTimeList = patient.get("dailyWearTimeList");
//
//        return Patient.builder()
//                .patientNum(patientNum)
//                .id(id)
//                .password(password)
//                .name(name)
//                .birthDate(Date.valueOf(birthDate))
//                .startDate(Date.valueOf(startDate))
//                .endDate("null".equals(endDate) ? null : Date.valueOf(endDate))
//                .dailyWearTimeList(jsonToDailyWearTimeList((JSONArray) dailyWearTimeList))
//                .build();
//    }

    // JSON 데이터를 DailyWearTime으로 변환
    private List<DailyWearTime> jsonToDailyWearTimeList(JSONArray jsonArray) throws JSONException {
        List<DailyWearTime> dailyWearTimeList = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject data = jsonArray.getJSONObject(i);

            dailyWearTimeList.add(DailyWearTime.builder()
                                                .id(data.getInt("id"))
                                                .patientNum(data.getString("patientNum"))
                                                .wearDate(Date.valueOf(data.getString("wearDate")))
                                                .totalWearTime(data.getLong("totalWearTime"))
                                                .build());
        }

        return dailyWearTimeList;
    }
}
