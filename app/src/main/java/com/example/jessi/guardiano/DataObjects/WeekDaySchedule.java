package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/22/2017.
 */

public abstract class WeekDaySchedule extends Schedule {

    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;

    public String getMonday() {
        return monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setMonday(String m) {
        this.monday = m;
    }

    public void setTuesday(String t) {
        this.tuesday = t;
    }

    public void setWednesday(String w) {
        this.wednesday = w;
    }

    public void setThursday(String th) {
        this.thursday = th;
    }

    public void setFriday(String f) {
        this.friday = f;
    }
}
