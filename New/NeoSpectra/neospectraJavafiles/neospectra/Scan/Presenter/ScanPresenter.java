package com.si_ware.neospectra.Scan.Presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si_ware.neospectra.DataIO.DataIO;
import com.si_ware.neospectra.ML_Library.Module.Module;
import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.Models.dbResult;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static com.si_ware.neospectra.DummyClassForTesting.getRandomDouble;
import static com.si_ware.neospectra.Global.Global.FILE_NAME_RESULTS;

/**
 * Created by AmrWinter on 1/9/18.
 */

public class ScanPresenter implements IScanPresenter {
    @Override
    public dbReading getSensorReading() {
        ArrayList<Double> sensorReading = new ArrayList<>();
        dbReading currentReading = dbReading.getInstance();

        //Simulate reading
        for (int i = 0; i < 340; i++) {
            sensorReading.add(getRandomDouble(0, 100));
        }
        currentReading.setReading(sensorReading);
        return currentReading;
    }

    @Override
    public dbResult scan(dbModule currentModule) {
        Module module = new Module(currentModule);
        return module.run(getSensorReading());
    }

    @Override
    public void saveResult(Context context, dbResult result) {
        ArrayList<dbResult> listOfResults;
        if (new File(context.getFilesDir(), FILE_NAME_RESULTS).exists()){
            //Get old results
            listOfResults = readResults(context);
        }
        else listOfResults = new ArrayList<>();
        //Append the new results to old results
        listOfResults.add(result);
        // To retrieve the object type
        Type type = new TypeToken<ArrayList<dbResult>>(){}.getType();
        String jsonData = new Gson().toJson(listOfResults, type);
        Log.v("Gson", jsonData);
        DataIO.writeStringAsFile(context, FILE_NAME_RESULTS, jsonData);
    }

    @Override
    public ArrayList<dbResult> readResults(Context mContext) {
        // The type of dbResult that stored later in the file
        Type type = new TypeToken<ArrayList<dbResult>>(){}.getType();
        String result = DataIO.readFileAsString(mContext, FILE_NAME_RESULTS);
        return new Gson().fromJson(result, type);
    }

    @Override
    public void displayResults(Context mContext, ArrayList<dbResult> results) {
        HashMap<String, String> headerHash; // timeStamp, ModuleName
        HashMap <String, ArrayList<dbResult.dbVarResult>> bodyList; // <timeStamp, arrayList<dbVarResult>>

        headerHash = new HashMap<>();
        bodyList = new HashMap<>();

        // init Headers
        for (dbResult result :
                results) {
            headerHash.put(result.getTimestamp(), result.getModuleName());
            bodyList.put(result.getTimestamp(), result.getResults());
        }
//        ExpandableListAdapter listAdapter;
//        return new ExpandableAdapter(mContext, headerHash, bodyList);
    }
}
