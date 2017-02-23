package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/19/2017.
 */

public class Plan {

    private String planName;
    private String planStart;

    public Plan() {

    }

    public Plan (String _planName){

        this.planName = _planName;
    }

    public Plan (String _planName, String _planStart){

        this.planName = _planName;
        this.planStart = _planStart;
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

    public void setPlandStart(String ps) {
        this.planStart = ps;
    }
}
