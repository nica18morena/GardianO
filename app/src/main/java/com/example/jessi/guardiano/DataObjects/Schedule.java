package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/21/2017.
 */

public abstract class Schedule {

    private String frequency;
    private String pickUpTime;
    private String dropOffTime;

    public String getFrequency() {
        return frequency;
    }
    public String getPickUpTime() {
        return pickUpTime;
    }
    public String getDropOffTime() {
        return dropOffTime;
    }
    public void setFrequency(String f) {
        this.frequency = f;
    }
    public void setPickUpTime(String put) {
        this.pickUpTime = put;
    }
    public void setDropOffTime(String dot) {
        this.dropOffTime = dot;
    }
}
