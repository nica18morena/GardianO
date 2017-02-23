package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/21/2017.
 */

public class SchoolScheduleWeekend extends Schedule {

    public SchoolScheduleWeekend(){

    }

    public SchoolScheduleWeekend(String f, String put, String dot){
        this.setFrequency(f);
        this.setPickUpTime(put);
        this.setDropOffTime(dot);
    }
}
