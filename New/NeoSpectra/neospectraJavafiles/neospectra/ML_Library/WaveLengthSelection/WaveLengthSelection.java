package com.si_ware.neospectra.ML_Library.WaveLengthSelection;

import android.content.Context;
import android.util.Log;

import com.si_ware.neospectra.Global.Factory;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.R;

import java.util.ArrayList;

/**
 * Created by AmrWinter on 1/13/18.
 */

public class WaveLengthSelection {
    public static dbReading do_Selection(ArrayList<Boolean> waveLengthVector, dbReading reading){
        dbReading newReading = dbReading.getInstance();
        ArrayList<Double> newReadingArray = new ArrayList<>();
        ArrayList<Double> oldReadingArray;
        oldReadingArray = reading.getReading();
        if (waveLengthVector.size() != oldReadingArray.size()){
            Log.e("err_WaveLength", "Selection vector doesn't match the Sensor vector");
            return reading;
        }
        else {
            for (int i = 0; i < waveLengthVector.size(); i++) {
                if (waveLengthVector.get(i)){
                    newReadingArray.add(oldReadingArray.get(i));
                }
            }
            newReading.setReading(newReadingArray);
        }
        return newReading;
    }
}
