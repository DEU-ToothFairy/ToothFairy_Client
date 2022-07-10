package com.example.toothfairy.model.util;

import com.example.toothfairy.model.data.WearingTime;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class WearingTimeManager {

    public static ArrayList<WearingTime> getWearingTimeList(JSONObject jsonObject){

        JSONArray times = (JSONArray) jsonObject.get("times");


        ArrayList<WearingTime> wearingTimes = new ArrayList<>();
        for (Object time : times) {
            Long start  = (Long)((JSONObject) time).get("start");
            Long end    = (Long)((JSONObject) time).get("end");

            wearingTimes.add(new WearingTime(start, end));
        }

        return wearingTimes;
    }
}
