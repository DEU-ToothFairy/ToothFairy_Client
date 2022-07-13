package com.example.toothfairy.data;

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
public class WearingTime {
    Long start; // milliseconds
    Long end;   // milliseconds

    public Long getWearingTime(){
        return end - start;
    }
    public int getHour(){
        return (int)((end - start) / (1000 * 60)) / 60;
    }
    public int getMinute(){
        return (int)((end - start) / (1000 * 60)) % 60;
    }
}