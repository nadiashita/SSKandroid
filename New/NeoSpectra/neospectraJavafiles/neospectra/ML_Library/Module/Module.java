package com.si_ware.neospectra.ML_Library.Module;

import com.si_ware.neospectra.Global.Global;
import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Models.dbModuleConfig;
import com.si_ware.neospectra.Models.dbVariableConfig;
import com.si_ware.neospectra.Models.dbModuleMetaData;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.Models.dbResult;

/**
 * Created by AmrWinter on 1/9/18.
 */

public class Module {
    dbModule currentModule;
    private IModule mModule;

    public Module(dbModule currentModule) {
        this.currentModule = currentModule;
    }

    public dbResult run(dbReading reading) {
        int TYPE = currentModule.getModuleType();
        switch (TYPE){
            case Global.TYPE_MODULE_CLASSIFICATION:
                mModule = new ClassificationModule(currentModule);
                break;
            case Global.TYPE_MODULE_REGRESSION:
                mModule = new RegressionModule(currentModule);
                break;
        }
        return mModule.run(reading);
    }
}
