package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/22/2017.
 */

public class UnderSchoolAgeScheduleWeekend extends Schedule {

    public UnderSchoolAgeScheduleWeekend(){

    }

    public UnderSchoolAgeScheduleWeekend(String f, String put, String dot){
        this.setFrequency(f);
        this.setPickUpTime(put);
        this.setDropOffTime(dot);
    }
}
