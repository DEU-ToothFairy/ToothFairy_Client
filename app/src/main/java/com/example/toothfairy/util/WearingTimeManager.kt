package com.example.toothfairy.util

import kotlin.Throws
import org.json.JSONException
import com.example.toothfairy.data.WearingTime
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

object WearingTimeManager {

    @Throws(JSONException::class)
    fun getWearingTimeList(jsonObject: JSONObject): ArrayList<WearingTime> {
        val times = jsonObject["times"] as JSONArray
        val wearingTimes = ArrayList<WearingTime>()

        for (i in 0 until times.length()) {
            val time:JSONObject = times.getJSONObject(i)

            val start   = time["start"] as Long
            val end     = time["end"] as Long

            wearingTimes.add(WearingTime(start, end))
        }

        return wearingTimes
    }
}