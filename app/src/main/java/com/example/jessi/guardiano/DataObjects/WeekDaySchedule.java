package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/22/2017.
 */

public abstract class WeekDaySchedule extends Schedule {

    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;

    public boolean getMonday() {
        return monday;
    }

    public boolean getTuesday() {
        return tuesday;
    }

    public boolean getWednesday() {
        return wednesday;
    }

    public boolean getThursday() {
        return thursday;
    }

    public boolean getFriday() {
        return friday;
    }

    public void setMonday(boolean m) {
        this.monday = m;
    }

    public void setTuesday(boolean t) {
        this.tuesday = t;
    }

    public void setWednesday(boolean w) {
        this.wednesday = w;
    }

    public void setThursday(boolean th) {
        this.thursday = th;
    }

    public void setFriday(boolean f) {
        this.friday = f;
    }
}
