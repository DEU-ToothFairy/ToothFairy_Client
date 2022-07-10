package com.example.toothfairy.model.repository;

import com.example.toothfairy.entity.DailyWearTime;
import com.example.toothfairy.entity.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository extends Repository {
    // 싱글턴 패턴
    public static PatientRepository patientRepository = new PatientRepository();
    public static PatientRepository getInstance(){ return patientRepository; }

    // VARIABLE
    private String URL = CONTEXT_PATH + "/api/patients";

    private PatientRepository() {}

//    public List<Patient> getAllPatients(){
//        List<Patient> patients = new ArrayList<>();
//
//        try{
//            Response response = this.get(URL);
//
//            //Response형 -> String형
//            String data = response.body().string();
//
//            JSONArray patientArray = new JSONArray(data);
//
//            // 응답으로 받은 JSON을 Patient 타입으로 변환
//            for (int i = 0; i < patientArray.length(); i++){
//                JSONObject patient = patientArray.getJSONObject(i);
//
//                patients.add(jsonToPatient(patient));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return patients;
//    }

//    public Patient getPatient(String patientNum){
//        try{
//            Response response = this.get(URL + "/" + patientNum);
//            String data = response.body().string();
//
//            JSONObject patient = new JSONObject(data);
//
//            return jsonToPatient(patient);
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }

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
