package com.si_ware.neospectra;

import android.content.Context;
import android.util.Log;

import com.si_ware.neospectra.Global.Global;
import com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing;
import com.si_ware.neospectra.Models.dbDataMatrix;
import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Models.dbVariableConfig;
import com.si_ware.neospectra.Models.dbModuleMetaData;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.Models.dbResult;
import com.si_ware.neospectra.Models.dbVarMetaData;
import com.si_ware.neospectra.Models.dbVariable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Covariance;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Derivative_1st;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Histograms;
import static com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction.TYPE_FeatureExtraction_Integral;
import static com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing.TYPE_PreProcessing_EMSC;
import static com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing.TYPE_PreProcessing_OPLEC;
import static com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing.TYPE_PreProcessing_OSC1;
import static com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing.TYPE_PreProcessing_OSC2;

/**
 * Created by AmrWinter on 12/17/2017.
 */

public class DummyClassForTesting {

    static public float getRandomFloat(double low, double high){
        return (float) ((float) new Random().nextDouble() * (high - low) + low);
    }

    static public double getRandomDouble(double low, double high){
        return new Random().nextDouble() * (high - low) + low;
    }

    public static String [] listOfModuleNames = {"Milk", "Coffee", "Gluten"};

    public static dbModule getRandomModule(Context mContext, String moduleName){
        dbModule module = new dbModule(moduleName);
        module.setModuleType(Global.TYPE_MODULE_REGRESSION);
        ArrayList<dbVariable> moduleVariables = new ArrayList<>();

        moduleVariables.add(
                makeRandomVariable(mContext.getString(R.string.protein),
                        TYPE_PreProcessing_OPLEC,
                        TYPE_FeatureExtraction_Integral));
        moduleVariables.add(
                makeRandomVariable(mContext.getString(R.string.lactose),
                        TYPE_PreProcessing_OSC1,
                        TYPE_FeatureExtraction_Derivative_1st));
        moduleVariables.add(
                makeRandomVariable(mContext.getString(R.string.fats),
                        TYPE_PreProcessing_EMSC,
                        TYPE_FeatureExtraction_Covariance));
        moduleVariables.add(
                makeRandomVariable(mContext.getString(R.string.solids),
                        TYPE_PreProcessing_OSC2,
                        TYPE_FeatureExtraction_Histograms));
        module.setModuleVariables(moduleVariables);
        return module;
    }

    private static dbVariable makeRandomVariable(String name, int typePre, int typeFeature) {
        // Variable
        dbVariable variable;
        // Variables meta-data
        dbVarMetaData varMetaData;
        //Variable Configuration
        dbVariableConfig varConfig;

        //Fats variable
        //Var Meta-data
        varMetaData = new dbVarMetaData(name);
        varMetaData.setVarLowerRange(getRandomFloat(0, 10));
        varMetaData.setVarUpperRange(getRandomFloat(100, 200));
        //Var Configs
        varConfig = new dbVariableConfig(name);
        varConfig.setFeatureExtraction_Type(typeFeature);
        varConfig.setPreProcessing_Type(typePre);
        //Add variable metadata and variable configuration to variable object
        variable = new dbVariable(name);
        variable.setVariableConfig(varConfig);
        variable.setVarMetaData(varMetaData);
        variable.setVariableData(getDataMatrix());
        return variable;
    }

    private static dbDataMatrix getDataMatrix() {
        // Matrix with random data for testing, simulates mik data.
        dbDataMatrix tmpDataMatrix = new dbDataMatrix();
        ArrayList<Double[]> tmp = new ArrayList<>();
        tmp.add(new Double[]
                {getRandomDouble(10, 70), getRandomDouble(10, 70), getRandomDouble(10, 70)});
        tmp.add(new Double[]
                {getRandomDouble(10, 70), getRandomDouble(10, 70), getRandomDouble(10, 70)});
        tmp.add(new Double[]
                {getRandomDouble(10, 70), getRandomDouble(10, 70), getRandomDouble(10, 70)});
        tmpDataMatrix.setData(tmp);
        Log.v("mdl", tmpDataMatrix.toString());
        return tmpDataMatrix;
    }

    public static dbResult testingResults(dbModule module) {
        ArrayList<dbVariable> variables;
        variables = module.getModuleVariables();
        dbResult result = new dbResult(module.getModuleName());
        for (dbVariable var: variables){
            result.addVariableResult(var.getVarName(),
                    getRandomDouble(var.getVarMetaData().getVarLowerRange(),
                            var.getVarMetaData().getVarUpperRange()));
        }
        return result;
    }

    public static ArrayList<dbModule> getTestingUserModules(Context mContext){
        List<dbModule> moduleList = new ArrayList<>();
        for (String name : listOfModuleNames) {
            moduleList.add(getRandomModule(mContext, name));
        }
        return new ArrayList<>(moduleList);
    }
}
