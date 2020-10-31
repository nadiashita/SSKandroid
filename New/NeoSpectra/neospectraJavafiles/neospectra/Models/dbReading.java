package com.si_ware.neospectra.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AmrWinter on 12/28/17.
 */

public class dbReading {
    private ArrayList<Double> reading;
    private static final dbReading ourInstance = new dbReading();

    public static dbReading getInstance() {
        return ourInstance;
    }

    private dbReading() {
    }

    public ArrayList<Double> getReading() {
        return reading;
    }

    public void setReading(ArrayList<Double> reading) {
        this.reading = reading;
    }

    @Override
    public String toString() {
        return "dbReading{" +
                "reading=" + reading +
                '}';
    }
}
