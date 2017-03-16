package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/19/2017.
 */

public class Plan {

    private String planName;
    private String planStart;
    private long planCalendar;


    public Plan() {

    }

    public Plan (String _planName){

        this.planName = _planName;
    }

    public Plan (String _planName, String _planStart){

        this.planName = _planName;
        this.planStart = _planStart;
    }
    public Plan (String _planName, long _planCalendar){

        this.planName = _planName;
        this.planCalendar = _planCalendar;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String pn){
        this.planName = pn;
    }

    public String getPlanStart() {
        return planStart;
    }

    public void setPlanStart(String ps) {
        this.planStart = ps;
    }

    public long getPlanCalendar() {
        return planCalendar;
    }

    public void setPlanCalendar(long pc){
        this.planCalendar = pc;
    }
}
