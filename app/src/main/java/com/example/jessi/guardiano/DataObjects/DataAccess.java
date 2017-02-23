package com.example.jessi.guardiano.DataObjects;

/**
 * Created by jessi on 2/22/2017.
 */
public class DataAccess {
    private static DataAccess ourInstance = new DataAccess();

    public static DataAccess getInstance() {
        return ourInstance;
    }

    private DataAccess() {
    }
}
