package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/21/2017.
 */

public abstract class Schedule {

    private String frequency;
    private String pickUpTime;
    private String dropOffTime;
    private long eventId;

    public String getFrequency() {
        return frequency;
    }
    public String getPickUpTime() {
        return pickUpTime;
    }
    public String getDropOffTime() {
        return dropOffTime;
    }
    public long getEventId(){ return eventId; }
    public void setFrequency(String f) {
        this.frequency = f;
    }
    public void setPickUpTime(String put) {
        this.pickUpTime = put;
    }
    public void setDropOffTime(String dot) {
        this.dropOffTime = dot;
    }
    public void setEventId(long eId){ this.eventId = eId; };

}
