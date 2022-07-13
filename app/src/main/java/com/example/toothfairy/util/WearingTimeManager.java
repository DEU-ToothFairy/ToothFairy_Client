package com.example.toothfairy.util;

import com.example.toothfairy.data.WearingTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WearingTimeManager {

    public static ArrayList<WearingTime> getWearingTimeList(JSONObject jsonObject) throws JSONException {

        JSONArray times = (JSONArray) jsonObject.get("times");

        ArrayList<WearingTime> wearingTimes = new ArrayList<>();

        for (int i = 0; i < times.length(); i++){
            JSONObject time = times.getJSONObject(i);

            Long start  = (Long)time.get("start");
            Long end    = (Long)time.get("end");

            wearingTimes.add(new WearingTime(start, end));
        }

        return wearingTimes;
    }
}
