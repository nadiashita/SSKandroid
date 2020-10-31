package com.si_ware.neospectra.Scan.Presenter;

import android.content.Context;

import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.Models.dbResult;

import java.util.ArrayList;

/**
 * Created by AmrWinter on 1/9/18.
 */

public interface IScanPresenter {
    //Get reading from the sensor
    dbReading getSensorReading();
    dbResult scan(dbModule currentModule);
    void saveResult(Context mContext, dbResult result);
    ArrayList<dbResult> readResults(Context mContext);
    void displayResults(Context mContext, ArrayList<dbResult> results);
}
