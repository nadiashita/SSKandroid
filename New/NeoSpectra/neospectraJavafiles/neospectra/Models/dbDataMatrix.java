package com.si_ware.neospectra.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by AmrWinter on 12/28/17.
 */

public class dbDataMatrix {
    private ArrayList<Double[]> data;

    public dbDataMatrix(/*ArrayList<Double[]> data*/) {
//        this.data = data;
    }

    public ArrayList<Double[]> getData() {
        return data;
    }

    public void setData(ArrayList<Double[]> data) {
        this.data = data;
    }

//    public String getStrData(){
//
//        return new Double(){[1.0, 58.0], [4.5, 5.1]};
//    }

    @Override
    public String toString() {
        return "\"dbDataMatrix\":{" +
                "\"data\":" + data.toString() +
                '}';
    }
}
