package com.example.toothfairy.data

import com.example.toothfairy.util.TimeManager

class WearingStats(var avgWearingTime: Long, var maxWearingTime: Long, var minWearingTime: Long) {

    val maxWearingTimeToString: String
        get() = TimeManager.getTimeToString(maxWearingTime)
    val minWearingTimeToString: String
        get() = TimeManager.getTimeToString(minWearingTime)
    val avgWearingTimeToString: String
        get() = TimeManager.getTimeToString(avgWearingTime)
}