package com.si_ware.neospectra.ML_Library.Module;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si_ware.neospectra.DataIO.DataIO;
import com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction;
import com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing;
import com.si_ware.neospectra.ML_Library.WaveLengthSelection.RunWaveLengthSelection;
import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Models.dbModuleConfig;
import com.si_ware.neospectra.Models.dbVariableConfig;
import com.si_ware.neospectra.Models.dbModuleMetaData;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.Models.dbResult;

import java.lang.reflect.Type;

import static com.si_ware.neospectra.DummyClassForTesting.testingResults;

/**
 * Created by AmrWinter on 1/7/18.
 */

public class ClassificationModule implements IModule {

    private dbModule currentModule;

    public ClassificationModule(dbModule currentModule) {
        this.currentModule = currentModule;
    }

    @Override
    public dbResult run(dbReading reading) {
        dbResult result;

        // TODO: 1/10/18 Replace the next line for real results
        result = testingResults(currentModule);
        return result;
    }

//    @Override
//    public void displayResults() {
//        throw new java.lang.UnsupportedOperationException("Not implemented yet.");
//    }
//
//    @Override
//    public void saveResults(Context context, dbModuleMetaData moduleMetaData, dbResult result) {
//        Gson gson = new Gson();
//        // To empower the lib to retrieve the object type
//        Type type = new TypeToken<dbResult>(){}.getType();
//        String jsonData = gson.toJson(result, type);
//        Log.v("Gson", jsonData);
//        String fileName = moduleMetaData.getModuleName();
//        DataIO.saveJsonData2File(context, fileName, jsonData);
//    }
//
//    @Override
//    public dbResult readResults(Context mContext, dbModuleMetaData moduleMetaData) {
//        String fileName = moduleMetaData.getModuleName();
//        Gson gson = new Gson();
//        // The type of dbResult that stored later in the file
//        Type type = new TypeToken<dbResult>(){}.getType();
//        String result = DataIO.readJsonDataFromFile(mContext, fileName);
//        dbResult tmpList = gson.fromJson(result, type);
//        return tmpList;
//    }
}