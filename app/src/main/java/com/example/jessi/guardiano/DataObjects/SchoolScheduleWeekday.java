package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/22/2017.
 */

public class SchoolScheduleWeekday extends WeekDaySchedule {

    public SchoolScheduleWeekday(){

    }
    public SchoolScheduleWeekday(String fq, String m, String t, String w,
                                 String th, String f, String put, String dot){

        this.setFrequency(fq);
        this.setMonday(m);
        this.setTuesday(t);
        this.setWednesday(w);
        this.setThursday(th);
        this.setFriday(f);
        this.setPickUpTime(put);
        this.setDropOffTime(dot);
    }
}
