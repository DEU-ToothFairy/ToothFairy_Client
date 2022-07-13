package com.example.toothfairy.model.repository;

import com.example.toothfairy.data.WearingStats;
import com.example.toothfairy.util.PreferenceManager;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WearingInfoRepository {
    public static WearingInfoRepository wearingInfoRepository = new WearingInfoRepository();
    public static WearingInfoRepository getInstance() { return wearingInfoRepository; }

    private PreferenceManager prefs;

    private final String ON = "on";
    private final String OFF = "off";
    private final String DAILY_WEARING_TIME = "dailyWearingTime";
    private final String AVG_WEARING_TIME   = "avgWearingTime";
    private final String MAX_WEARING_TIME   = "maxWearingTime";
    private final String MIN_WEARING_TIME   = "minWearingTime";

    private WearingInfoRepository(){ }

    public void init(String patientId){
        // 사용자 이름의 아이디로 내부 DB 생성
        prefs = new PreferenceManager(patientId);
    }

    // 교정 장치 착용 감지 시
    public void setOn(){
        // ON을 받은
        prefs.setLong(ON, System.currentTimeMillis());
    }

    // 교정 장치 착용 해제 시
    public Long setOff(){
        Long onTime = prefs.getLong(ON);
        Long currentTime = System.currentTimeMillis();

        if(onTime == 0L) return getDailyWearingTime();

        // OFF를 받으면 이전의 ON은 지워줌
        prefs.removeKey(ON);

        // 착용 시간 저장
        return setDailyWearingTime(Math.abs(currentTime - onTime));
    }

    public Long setDailyWearingTime(Long time){
        Long savedTime = prefs.getLong(DAILY_WEARING_TIME);

        prefs.setLong(DAILY_WEARING_TIME, savedTime + time);

        return prefs.getLong(DAILY_WEARING_TIME);
    }

    // 하루 평균 착용 시간 저장
    public Long setAvgWearingTime(Long time){
        Long avgTime = prefs.getLong(AVG_WEARING_TIME);

        // 저장된 값이 없으면 바로 대입
        if(avgTime == 0L) {
            prefs.setLong(AVG_WEARING_TIME ,time);

            return time;
        }
        else{
            avgTime = (avgTime + time) / 2; // 평균 계산
            prefs.setLong(AVG_WEARING_TIME, avgTime);

            return avgTime;
        }
    }

    // 최대 착용 시간 저장
    public Long setMaxWearingTime(Long time){
        Long maxTime = prefs.getLong(MAX_WEARING_TIME);

        prefs.setLong(MAX_WEARING_TIME, Math.max(maxTime, time));

        return prefs.getLong(MAX_WEARING_TIME);
    }

    public Long setMinWearingTime(Long time){
        Long minTime = prefs.getLong(MIN_WEARING_TIME);

        prefs.setLong(MIN_WEARING_TIME, Math.min(minTime, time));
        return prefs.getLong(MIN_WEARING_TIME);
    }

    public WearingStats getWearingStats(){
        Long avg = prefs.getLong(AVG_WEARING_TIME);
        Long max = prefs.getLong(MAX_WEARING_TIME);
        Long min = prefs.getLong(MIN_WEARING_TIME);

        return new WearingStats(avg, max ,min);
    }

    public Long getDailyWearingTime(){
        return prefs.getLong(DAILY_WEARING_TIME);
    }
}

