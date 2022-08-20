package com.example.toothfairy.model.repository

import com.example.toothfairy.data.WearingStats
import com.example.toothfairy.util.PreferenceManager
import kotlin.math.abs

object WearingInfoRepository {
    private const val ON = "on"
    private const val OFF = "off"
    private const val DAILY_WEARING_TIME = "dailyWearingTime"
    private const val AVG_WEARING_TIME = "avgWearingTime"
    private const val MAX_WEARING_TIME = "maxWearingTime"
    private const val MIN_WEARING_TIME = "minWearingTime"

    private var prefs: PreferenceManager? = null

    fun init(patientId: String?) {
        // 사용자 이름의 아이디로 내부 DB 생성
        if (prefs == null) prefs = PreferenceManager(patientId)
    }

    // 교정 장치 착용 감지 시
    fun setOn() {
        // ON을 받은
        prefs!!.setLong(ON, System.currentTimeMillis())
    }

    // 교정 장치 착용 해제 시
    fun setOff(): Long {
        val onTime = prefs!!.getLong(ON)
        val currentTime = System.currentTimeMillis()

        if (onTime == 0L) return dailyWearingTime

        // OFF를 받으면 이전의 ON은 지워줌
        prefs!!.removeKey(ON)

        // 착용 시간 저장
        return setDailyWearingTime(abs(currentTime - onTime))
    }

    fun setDailyWearingTime(time: Long): Long {
        val savedTime = prefs!!.getLong(DAILY_WEARING_TIME)
        prefs!!.setLong(DAILY_WEARING_TIME, savedTime + time)

        return prefs!!.getLong(DAILY_WEARING_TIME)
    }

    // 하루 평균 착용 시간 저장
    fun setAvgWearingTime(time: Long): Long {
        var avgTime = prefs!!.getLong(AVG_WEARING_TIME)

        // 저장된 값이 없으면 바로 대입
        return if (avgTime == 0L) {
            prefs!!.setLong(AVG_WEARING_TIME, time)

            time
        } else {
            avgTime = (avgTime + time) / 2 // 평균 계산
            prefs!!.setLong(AVG_WEARING_TIME, avgTime)

            avgTime
        }
    }

    // 최대 착용 시간 저장
    fun setMaxWearingTime(time: Long?): Long {
        val maxTime = prefs!!.getLong(MAX_WEARING_TIME)

        prefs!!.setLong(MAX_WEARING_TIME, maxTime.coerceAtLeast(time!!)) // coerceAtLeast : 호출된 객체가 특정 객체보다 큰 지 확인 (Math.max(maxTime, time))

        return prefs!!.getLong(MAX_WEARING_TIME)
    }
    
    // 최소 착용 시간 저장
    fun setMinWearingTime(time: Long?): Long {
        val minTime = prefs!!.getLong(MIN_WEARING_TIME)

        if (minTime == 0L) prefs!!.setLong(MIN_WEARING_TIME, time!!)
        else prefs!!.setLong(MIN_WEARING_TIME, Math.min(minTime, time!!))

        return prefs!!.getLong(MIN_WEARING_TIME)
    }

    val wearingStats: WearingStats
        get() {
            val avg = prefs!!.getLong(AVG_WEARING_TIME)
            val max = prefs!!.getLong(MAX_WEARING_TIME)
            val min = prefs!!.getLong(MIN_WEARING_TIME)

            return WearingStats(avg, max, min)
        }

    val dailyWearingTime: Long
        get() = prefs!!.getLong(DAILY_WEARING_TIME)
}