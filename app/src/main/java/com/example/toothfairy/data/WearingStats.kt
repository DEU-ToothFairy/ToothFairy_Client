package com.example.toothfairy.data

import com.example.toothfairy.util.DateManager
import lombok.*

class WearingStats(var avgWearingTime: Long, var maxWearingTime: Long, var minWearingTime: Long) {

    val maxWearingTimeToString: String
        get() = DateManager.getTimeToString(maxWearingTime)
    val minWearingTimeToString: String
        get() = DateManager.getTimeToString(minWearingTime)
    val avgWearingTimeToString: String
        get() = DateManager.getTimeToString(avgWearingTime)
}