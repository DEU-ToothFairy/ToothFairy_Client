package com.example.toothfairy.data;


import com.example.toothfairy.util.DateManager;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WearingStats {
    public Long avgWearingTime;
    public Long maxWearingTime;
    public Long minWearingTime;

    public String getMaxWearingTimeToString(){ return DateManager.getTimeToString(maxWearingTime); }
    public String getMinWearingTimeToString(){ return DateManager.getTimeToString(minWearingTime); }
    public String getAvgWearingTimeToString(){ return DateManager.getTimeToString(avgWearingTime); }
}
