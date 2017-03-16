package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/22/2017.
 */

public class UnderSchoolAgeScheduleWeekday extends WeekDaySchedule {

    public UnderSchoolAgeScheduleWeekday(){

    }

    public UnderSchoolAgeScheduleWeekday(String fq, boolean m, boolean t, boolean w,
                                         boolean th, boolean f, String put, String dot, long eId){

        this.setFrequency(fq);
        this.setMonday(m);
        this.setTuesday(t);
        this.setWednesday(w);
        this.setThursday(th);
        this.setFriday(f);
        this.setPickUpTime(put);
        this.setDropOffTime(dot);
        this.setEventId(eId);
    }
}
