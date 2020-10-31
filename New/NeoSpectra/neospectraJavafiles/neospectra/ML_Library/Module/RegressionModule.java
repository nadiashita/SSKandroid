package com.si_ware.neospectra.ML_Library.Module;

import com.si_ware.neospectra.ML_Library.FeatureExtraction.RunFeatureExtraction;
import com.si_ware.neospectra.ML_Library.PreProcessing.RunPreProcessing;
import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.Models.dbResult;
import com.si_ware.neospectra.Models.dbVariable;
import com.si_ware.neospectra.Models.dbVariableConfig;

import static com.si_ware.neospectra.DummyClassForTesting.testingResults;

/**
 * Created by AmrWinter on 1/7/18.
 */

public class RegressionModule implements IModule {

    private dbModule currentModule;

    public RegressionModule(dbModule currentModule) {
        this.currentModule = currentModule;
    }

    @Override
    public dbResult run(dbReading reading) {
        dbResult result = new dbResult(currentModule.getModuleName());

        for (dbVariable variable: currentModule.getModuleVariables()) {
            result.addVariableResult(variable.getVarName(), do_Scan(variable.getVariableConfig()));
        }

        // TODO: 1/10/18 Replace the next line for real results
        result = testingResults(currentModule);
        return result;
    }

    private double do_Scan(dbVariableConfig variable) {
        // Run PreProcessing
        RunPreProcessing.run(variable.getPreProcessing_Type());
        //Run WaveLength Selection
//        RunWaveLengthSelection.run(variable.getWaveLength_Type());
        //Run Feature Extraction
        RunFeatureExtraction.run(variable.getFeatureExtraction_Type());
        return 0;
    }
}