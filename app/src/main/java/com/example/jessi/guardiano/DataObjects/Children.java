package com.example.jessi.guardiano.DataObjects;


/**
 * Created by jessi on 2/19/2017.
 */

public class Children {

    private String childName;
    private String childDOB;

    public Children(){

    }

    public Children (String _childName, String _childDOB) {

        this.childName = _childName;
        this.childDOB = _childDOB;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String cn){
        this.childName = cn;
    }

    public String getChildDOB() {
        return childDOB;
    }

    public void setChildDOB(String cdob){
        this.childDOB = cdob;
    }
}

